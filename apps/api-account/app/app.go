package main

import (
	"api-account/internal/service"
	"fmt"
	"github.com/ansrivas/fiberprometheus/v2"
	"github.com/gofiber/fiber/v2"
	"github.com/rs/zerolog"
	"github.com/rs/zerolog/log"
	"os"
)

func main() {
	app := fiber.New(fiber.Config{
		DisableStartupMessage: true,
	})
	zerolog.TimestampFieldName = "date"
	zerolog.ErrorFieldName = "message"
	var appname = os.Getenv("APP_NAME")
	log.Logger = log.With().Str("application", appname).Logger()
	prometheus := fiberprometheus.New(appname)
	prometheus.RegisterAt(app, "/metrics")
	app.Use(prometheus.Middleware)
	app.Get("/accounts", func(c *fiber.Ctx) error {
		c.Accepts("application/json")
		name := c.Query("customer_id", "0")
		tid := c.Get("tid", "-")
		log.Info().Str("tid", tid).Msg(fmt.Sprintf("finding %s", name))
		account, err := service.GetAccount(name)
		log.Info().Str("tid", tid).Msg(fmt.Sprintf("found account %s", account.Id))
		if err != nil {
			return fiber.NewError(404, "nao encontrado")
		} else {
			return c.JSON(account)
		}
	})

	err := app.Listen(":3000")
	if err != nil {
		log.Error().Err(err).Msg("")
	}
}
