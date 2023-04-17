package main

import (
	"api-customer/internal/service"
	"github.com/ansrivas/fiberprometheus/v2"
	"github.com/gofiber/fiber/v2"
	"github.com/rs/zerolog"
	"github.com/rs/zerolog/log"
	"os"
)

func main() {
	app := fiber.New()
	zerolog.TimestampFieldName = "date"
	var appname = os.Getenv("APP_NAME")
	log.Logger = log.With().Str("application", appname).Logger()
	prometheus := fiberprometheus.New(appname)
	prometheus.RegisterAt(app, "/metrics")
	app.Use(prometheus.Middleware)
	app.Get("/customers", func(c *fiber.Ctx) error {
		c.Accepts("application/json")
		lc := service.GetCustomersAll()
		log.Info().Msg("finding all customers")
		return c.JSON(lc)
	})
	//app.Get("/customer/:id", func(c *fiber.Ctx) error {
	//	c.Accepts("application/json")
	//	name := c.Params("id", "")
	//	if name == "" {
	//		lc := service.GetCustomersAll()
	//		return c.JSON(lc)
	//	}
	//	println(name)
	//	customers, err := service.GetCustomers(name)
	//	if err != nil {
	//		return fiber.NewError(404, "nao encontrato")
	//	} else {
	//		return c.JSON(customers)
	//	}
	//})

	err := app.Listen(":3000")
	if err != nil {
		log.Err(err)
	}
}
