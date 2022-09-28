package main

import (
	"apppuc/internal/service"
	"github.com/gofiber/fiber/v2"
)

func main() {
	app := fiber.New()

	app.Get("/customer-offers/:id", func(c *fiber.Ctx) error {
		c.Accepts("application/json")
		name := c.Params("id", "")
		println(name)
		customers, err := service.GetCustomers(name)
		if err != nil {
			return err
		}
		return c.JSON(customers)
	})

	err := app.Listen(":3000")
	if err != nil {
		println(err)
	}
}
