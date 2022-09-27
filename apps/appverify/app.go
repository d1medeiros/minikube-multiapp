package main

import (
	"github.com/gofiber/fiber/v2"
)

type Response struct {
	Valid        bool   `json:"valid"`
	InternalCode string `json:"internal_code"`
}

func main() {
	app := fiber.New()

	app.Get("/verify/:id", func(c *fiber.Ctx) error {
		c.Accepts("application/json")
		name := c.Params("id", "")
		println(name)
		if name == "x3" {
			return c.Status(401).JSON(Response{
				Valid:        false,
				InternalCode: "",
			})
		}
		return c.Status(200).JSON(Response{
			Valid:        true,
			InternalCode: "fds3lfkj434dsafl",
		})
	})

	err := app.Listen(":3001")
	if err != nil {
		println(err)
	}
}
