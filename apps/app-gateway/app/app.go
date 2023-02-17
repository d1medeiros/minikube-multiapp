package main

import (
	"apppuc/internal/model"
	"encoding/json"
	"fmt"
	"github.com/go-resty/resty/v2"
	"github.com/gofiber/fiber/v2"
)

var app *fiber.App
var customerClient *resty.Client
var accountClient *resty.Client

func init() {
	app = fiber.New()
	customerClient = resty.New()
	accountClient = resty.New()
}

func main() {
	server()

}

func server() {
	app.Post("/run", func(c *fiber.Ctx) error {
		c.Accepts("application/json")
		var wrapper []model.Account
		cus := &[]model.Customer{}
		err := callGet[[]model.Customer]("http://apicustomer:3000/customers", cus)
		if err != nil {
			return err
		}
		list := *cus
		fmt.Println(list)
		for _, item := range list {
			fmt.Printf("customer %s", item.Name)
			acc := &model.Account{}
			err = callGet[model.Account](fmt.Sprintf("http://apiaccount:3001/accounts?customer_id=%s", item.Id), acc)
			if err != nil {
				return err
			}
			fmt.Printf("customer:%s account:%s ", item.Name, acc.Id)
			fr := &model.Fraud{}
			err = callGet[model.Fraud](fmt.Sprintf("http://apifraud:3002/frauds/%s", acc.Id), fr)
			if err != nil {
				return err
			}
			acc.Allowed = fr.Allowed
			of := &model.Offer{}
			err = callGet[model.Offer](fmt.Sprintf("http://apioffer:3003/offers?account_id=%s", acc.Id), of)
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
