package main

import (
	"api-fraud/internal/service"
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

	app.Get("/frauds/:id", func(c *fiber.Ctx) error {
		c.Accepts("application/json")
		name := c.Params("id", "0")
		println(name)
		isAllowed := service.Verify(name)
		if !isAllowed {
			return fiber.NewError(403, "nao permitido")
		}
		return c.JSON(`{"allowed": true}`)
	})

	err := app.Listen(":3000")
	if err != nil {
		println(err)
	}
}
