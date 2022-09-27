package main

import (
	"errors"
	"github.com/gofiber/fiber/v2"
)

type Customer struct {
	Id       string
	Eligible bool
	Limit    float64
}

func main() {
	app := fiber.New()

	app.Get("/customer-offers/:id", func(c *fiber.Ctx) error {
		c.Accepts("application/json")
		name := c.Params("id", "")
		println(name)
		if name == "x1" {
			json := Customer{Id: "x1", Eligible: false}
			return c.JSON(json)
		}
		if name == "y1" {
			json := Customer{Id: "y1", Eligible: true, Limit: 50000.0}
			return c.JSON(json)
		}
		return errors.New("not found")
	})

	err := app.Listen(":3000")
	if err != nil {
		println(err)
	}
}
