package main

import (
	"api-account/internal/service"
	"github.com/gofiber/fiber/v2"
)

func main() {
	app := fiber.New()

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
