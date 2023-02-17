package main

import (
	"api-customer/internal/service"
	"github.com/gofiber/fiber/v2"
)

func main() {
	app := fiber.New()

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
