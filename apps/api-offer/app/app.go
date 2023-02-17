package main

import (
	"api-offer/internal/server"
	"github.com/gofiber/fiber/v2"
)

func main() {
	app := fiber.New()
	err := server.Run(app, ":3000")
	if err != nil {
		println(err)
	}
}
