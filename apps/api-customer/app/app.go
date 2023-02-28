package main

import (
	"api-customer/internal/service"
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
	app.Get("/customers", func(c *fiber.Ctx) error {
		c.Accepts("application/json")
		lc := service.GetCustomersAll()
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
		println(err)
	}
}
