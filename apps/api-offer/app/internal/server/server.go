package server

import (
	"api-offer/internal/application/service"
	"api-offer/internal/errors"
	"fmt"
	"github.com/gofiber/fiber/v2"
	"github.com/rs/zerolog/log"
)

func Run(app *fiber.App, port string) error {
	app.Get("/offers", GetOffer)
	return app.Listen(port)
}

func GetOffer(c *fiber.Ctx) error {
	c.Accepts("application/json")
	id := c.Query("account_id", "-")
	tid := c.Get("tid", "-")
	offer, err := service.GetOffers(id)
	if errors.IsHttpError(err) {
		c.JSON(err.Error())
	}
	if err != nil {
		return err
	}
	log.Info().Str("tid", tid).Msg(fmt.Sprintf("offers size: %d", len(offer.Items)))
	c.JSON(*offer)
	return nil
}
