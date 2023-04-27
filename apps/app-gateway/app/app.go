package main

import (
	"apppuc/internal/model"
	"context"
	"encoding/json"
	"fmt"
	"github.com/rs/zerolog"
	"github.com/rs/zerolog/log"
	"go.opentelemetry.io/contrib/instrumentation/net/http/otelhttp"
	"go.opentelemetry.io/otel/attribute"
	"go.opentelemetry.io/otel/trace"
	"io/ioutil"
	"math/rand"
	"mylibs/pkg/mhttp"
	"net/http"
	"os"
	"reflect"
	"time"
	"unsafe"
)

var customerClient http.Client

var appname = os.Getenv("APP_NAME")
var customer_host = os.Getenv("CUSTOMER_ENDPOINT")
var account_host = os.Getenv("ACCOUNT_ENDPOINT")
var fraud_host = os.Getenv("FRAUD_ENDPOINT")
var offer_host = os.Getenv("OFFER_ENDPOINT")

func init() {
	customerClient = http.Client{
		Transport: otelhttp.NewTransport(http.DefaultTransport),
	}
}

func main() {

	zerolog.TimestampFieldName = "date"
	zerolog.ErrorFieldName = "message"
	log.Logger = log.With().Str("application", appname).Logger()

	//trace
	ow := mhttp.OTELWrapper{}
	tp, err := ow.TracerProvider(appname, "http://simplest-collector.monitoring:14268/api/traces")
	if err != nil {
		panic(err)
	}

	log.Info().Msg("gateway init")
	for true {
		tr := tp.Tracer("Gateway-tracer")
		ctx, span := tr.Start(context.Background(), "start.gateway")
		err := server(ctx, tr)
		span.End()
		if err != nil {
			log.Error().Err(err).Msg("")
		}
		time.Sleep(4 * time.Second)
	}
}

func server(ctx context.Context, tr trace.Tracer) error {
	tid := RandStringBytes(18)
	var wrapper []model.Account
	cus := &[]model.Customer{}
	err := callGet[[]model.Customer](ctx, tr, fmt.Sprintf("http://%s/customers", customer_host), cus, tid)
	if err != nil {
		return err
	}
	list := *cus
	for _, item := range list {
		acc := &model.Account{}
		err = callGet[model.Account](ctx, tr, fmt.Sprintf("http://%s/accounts?customer_id=%s", account_host, item.Id), acc, tid)
		if err != nil {
			return err
		}
		fr := &model.Fraud{}
		err = callGet[model.Fraud](ctx, tr, fmt.Sprintf("http://%s/frauds/%s", fraud_host, acc.Id), fr, tid)
		if err != nil {
			return err
		}
		acc.Allowed = fr.Allowed
		of := &model.Offer{}
		err = callGet[model.Offer](ctx, tr, fmt.Sprintf("http://%s/offers?account_id=%s", offer_host, acc.Id), of, tid)
		if err != nil {
			return err
		}
		acc.Items = of.Items
		wrapper = append(wrapper, *acc)
	}
	return nil
}

func callGet[T any](ctx context.Context, tr trace.Tracer, url string, t *T, tid string) error {
	innerCtx, span := tr.Start(ctx, fmt.Sprintf("GET:%s", url))
	span.SetAttributes(attribute.Key("tid").String(tid))
	defer span.End()
	log.Info().Msgf("call GET:%s", url)
	req, err := http.NewRequestWithContext(innerCtx, "GET", url, nil)
	res, err := customerClient.Do(req)
	if err != nil {
		log.Error().Err(err).Msg("")
		return err
	}
	resBody, err := ioutil.ReadAll(res.Body)
	if err != nil {
		log.Error().Err(err).Msg("")
		return err
	}
	err = json.Unmarshal(resBody, t)
	if err != nil {
		log.Error().Err(err).Msg("")
		return err
	}
	return nil
}

const letterBytes = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

func RandStringBytes(n int) string {
	b := make([]byte, n)
	for i := range b {
		b[i] = letterBytes[rand.Intn(len(letterBytes))]
	}
	return string(b)
}

func printContextInternals(ctx interface{}, inner bool) {
	contextValues := reflect.ValueOf(ctx).Elem()
	contextKeys := reflect.TypeOf(ctx).Elem()

	if !inner {
		fmt.Printf("\nFields for %s.%s\n", contextKeys.PkgPath(), contextKeys.Name())
	}

	if contextKeys.Kind() == reflect.Struct {
		for i := 0; i < contextValues.NumField(); i++ {
			reflectValue := contextValues.Field(i)
			reflectValue = reflect.NewAt(reflectValue.Type(), unsafe.Pointer(reflectValue.UnsafeAddr())).Elem()

			reflectField := contextKeys.Field(i)

			if reflectField.Name == "Context" {
				printContextInternals(reflectValue.Interface(), true)
			} else {
				fmt.Printf("field name: %+v\n", reflectField.Name)
				fmt.Printf("value: %+v\n", reflectValue.Interface())
			}
		}
	} else {
		fmt.Printf("context is empty (int)\n")
	}
}
