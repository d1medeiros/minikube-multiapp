package main

import (
	"api-fraud/internal/service"
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

	app.Get("/frauds/:id", func(c *fiber.Ctx) error {
		c.Accepts("application/json")
		name := c.Params("id", "0")
		tid := c.Get("tid", "-")
		log.Info().Str("tid", tid).Msg(name)
		isAllowed := service.Verify(name)
		if !isAllowed {
			return fiber.NewError(403, "nao permitido")
		}
		return c.JSON(`{"allowed": true}`)
	})

	err := app.Listen(":3000")
	if err != nil {
		log.Error().Err(err).Msg("")
	}
}
