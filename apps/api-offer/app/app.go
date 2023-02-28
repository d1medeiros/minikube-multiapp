package main

import (
	"api-offer/internal/server"
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

	err := server.Run(app, ":3000")
	if err != nil {
		println(err)
	}
}
