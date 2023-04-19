package main

import (
	"apppuc/internal/model"
	"encoding/json"
	"fmt"
	"github.com/ansrivas/fiberprometheus/v2"
	"github.com/go-resty/resty/v2"
	"github.com/gofiber/fiber/v2"
	"github.com/rs/zerolog"
	"github.com/rs/zerolog/log"
	"math/rand"
	"os"
	"time"
)

var app *fiber.App
var customerClient *resty.Client

var customer_host = os.Getenv("CUSTOMER_ENDPOINT")
var account_host = os.Getenv("ACCOUNT_ENDPOINT")
var fraud_host = os.Getenv("FRAUD_ENDPOINT")
var offer_host = os.Getenv("OFFER_ENDPOINT")

func init() {
	app = fiber.New()
	customerClient = resty.New().OnBeforeRequest(func(client *resty.Client, request *resty.Request) error {
		log.Info().Str("tid", request.Header.Get("tid")).Msg(request.URL)
		return nil
	})
}

func main() {
	zerolog.TimestampFieldName = "date"
	zerolog.ErrorFieldName = "message"
	var appname = os.Getenv("APP_NAME")
	log.Logger = log.With().Str("application", appname).Logger()
	prometheus := fiberprometheus.New(appname)
	prometheus.RegisterAt(app, "/metrics")
	app.Use(prometheus.Middleware)

	log.Info().Msg("gateway init")
	for true {
		err := server()
		if err != nil {
			log.Error().Err(err).Msg("")
		}
		time.Sleep(4 * time.Second)
	}
}

func server() error {
	tid := RandStringBytes(18)
	var wrapper []model.Account
	cus := &[]model.Customer{}
	err := callGet[[]model.Customer](fmt.Sprintf("http://%s/customers", customer_host), cus, tid)
	if err != nil {
		return err
	}
	list := *cus
	for _, item := range list {
		acc := &model.Account{}
		err = callGet[model.Account](fmt.Sprintf("http://%s/accounts?customer_id=%s", account_host, item.Id), acc, tid)
		if err != nil {
			return err
		}
		fr := &model.Fraud{}
		err = callGet[model.Fraud](fmt.Sprintf("http://%s/frauds/%s", fraud_host, acc.Id), fr, tid)
		if err != nil {
			return err
		}
		acc.Allowed = fr.Allowed
		of := &model.Offer{}
		err = callGet[model.Offer](fmt.Sprintf("http://%s/offers?account_id=%s", offer_host, acc.Id), of, tid)
		if err != nil {
			return err
		}
		acc.Items = of.Items
		wrapper = append(wrapper, *acc)
	}
	return nil
}

func callGet[T any](url string, t *T, tid string) error {
	res, err := customerClient.R().
		SetHeader("tid", tid).
		Get(url)
	if err != nil {
		return err
	}
	if res.IsError() {
		return err
	}
	b := fmt.Sprintf("%v", res)
	err = json.Unmarshal([]byte(b), t)
	if err != nil {
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
