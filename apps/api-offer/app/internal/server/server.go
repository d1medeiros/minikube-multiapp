package server

import (
	"github.com/gofiber/fiber/v2"
)

func Run(app *fiber.App, port string) error {
	return app.Listen(port)
}
