package main

import (
	"apppuc/internal/model"
	"context"
	"encoding/json"
	"fmt"
	"github.com/rs/zerolog"
	"github.com/rs/zerolog/log"
	"io/ioutil"
	"mylibs/pkg/observability/motel"
	"mylibs/pkg/util"
	"net/http"
	"os"
	"time"
)

var customerClient http.Client

var appname = os.Getenv("APP_NAME")
var traceEndpoint = os.Getenv("TRACE_ENDPOINT")
var customer_host = os.Getenv("CUSTOMER_ENDPOINT")
var account_host = os.Getenv("ACCOUNT_ENDPOINT")
var fraud_host = os.Getenv("FRAUD_ENDPOINT")
var offer_host = os.Getenv("OFFER_ENDPOINT")

func init() {
	customerClient = motel.NewClient()
}

func main() {

	zerolog.TimestampFieldName = "date"
	zerolog.ErrorFieldName = "message"
	log.Logger = log.With().Str("application", appname).Logger()

	//trace
	ow := motel.OTELWrapper{}
	err := ow.TracerProvider2(appname, traceEndpoint)
	if err != nil {
		panic(err)
	}

	log.Info().Msg("gateway init")
	for true {
		tr := ow.Tracer("Gateway-tracer")
		ctx, span := tr.Start(context.Background(), "start.gateway")
		err := server(ctx, tr)
		span.End()
		if err != nil {
			log.Error().Err(err).Msg("")
		}
		time.Sleep(4 * time.Second)
	}
}

func server(ctx context.Context, tr motel.MyTracer) error {
	tid := util.RandStringBytes(18)
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

func callGet[T any](ctx context.Context, tr motel.MyTracer, url string, t *T, tid string) error {
	innerCtx, span := tr.Start(ctx, fmt.Sprintf("GET:%s", url))
	span.SetAttributes("tid", tid)
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
