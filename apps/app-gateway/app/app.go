package main

import (
	"apppuc/internal/model"
	"encoding/json"
	"fmt"
	"github.com/ansrivas/fiberprometheus/v2"
	"github.com/go-resty/resty/v2"
	"github.com/gofiber/fiber/v2"
	"os"
)

var app *fiber.App
var customerClient *resty.Client
var accountClient *resty.Client

var customer_host = os.Getenv("CUSTOMER_ENDPOINT")
var account_host = os.Getenv("ACCOUNT_ENDPOINT")
var fraud_host = os.Getenv("FRAUD_ENDPOINT")
var offer_host = os.Getenv("OFFER_ENDPOINT")

func init() {
	app = fiber.New()
	customerClient = resty.New()
	accountClient = resty.New()
}

func main() {
	var appname = os.Getenv("APP_NAME")
	prometheus := fiberprometheus.New(appname)
	prometheus.RegisterAt(app, "/metrics")
	app.Use(prometheus.Middleware)

	server()

}

func server() {
	app.Post("/run", func(c *fiber.Ctx) error {
		c.Accepts("application/json")
		var wrapper []model.Account
		cus := &[]model.Customer{}
		err := callGet[[]model.Customer](fmt.Sprintf("http://%s/customers", customer_host), cus)
		if err != nil {
			return err
		}
		list := *cus
		fmt.Println(list)
		for _, item := range list {
			fmt.Printf("customer %s", item.Name)
			acc := &model.Account{}
			err = callGet[model.Account](fmt.Sprintf("http://%s/accounts?customer_id=%s", account_host, item.Id), acc)
			if err != nil {
				return err
			}
			fmt.Printf("customer:%s account:%s ", item.Name, acc.Id)
			fr := &model.Fraud{}
			err = callGet[model.Fraud](fmt.Sprintf("http://%s/frauds/%s", fraud_host, acc.Id), fr)
			if err != nil {
				return err
			}
			acc.Allowed = fr.Allowed
			of := &model.Offer{}
			err = callGet[model.Offer](fmt.Sprintf("http://%s/offers?account_id=%s", offer_host, acc.Id), of)
			if err != nil {
				return err
			}
			acc.Items = of.Items
			wrapper = append(wrapper, *acc)
		}
		c.JSON(wrapper)
		return nil
	})

	err := app.Listen(":3000")
	if err != nil {
		println(err)
	}
}

func callGet[T any](url string, t *T) error {
	res, err := customerClient.R().
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
