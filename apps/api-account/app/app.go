package main

import (
	"api-account/internal/service"
	"github.com/ansrivas/fiberprometheus/v2"
	"github.com/gofiber/fiber/v2"
	"os"
)

func main() {
	app := fiber.New()
	var appname = os.Getenv("APP_NAME")
	prometheus := fiberprometheus.New(appname)
	prometheus.RegisterAt(app, "/metrics")
	app.Use(prometheus.Middleware)
	app.Get("/accounts", func(c *fiber.Ctx) error {
		c.Accepts("application/json")
		name := c.Query("customer_id", "0")
		println(name)
		account, err := service.GetAccount(name)
		if err != nil {
			return fiber.NewError(404, "nao encontrado")
		} else {
			return c.JSON(account)
		}
	})

	err := app.Listen(":3000")
	if err != nil {
		println(err)
	}
}
