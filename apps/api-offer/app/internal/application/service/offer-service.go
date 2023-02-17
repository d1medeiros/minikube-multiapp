package service

import (
	"api-offer/internal/application/model"
	"api-offer/internal/errors"
)

var i1 = model.OfferItem{
	Name:  "Example offer 1",
	Value: 1030.0,
}
var i2 = model.OfferItem{
	Name:  "Example offer 2",
	Value: 7730.0,
}
var i3 = model.OfferItem{
	Name:  "Example offer 3",
	Value: 330.0,
}
var i4 = model.OfferItem{
	Name:  "Example offer 4",
	Value: 31030.0,
}

var mapper = []model.Offer{
	{
		AccountId: "abc",
		Items:     []model.OfferItem{i1, i2},
	},
	{
		AccountId: "cba",
		Items:     []model.OfferItem{i1, i2, i3, i4},
	},
}

func GetOffers(id string) (*model.Offer, error) {
	if id == "-" {
		return nil, errors.InternalError
	}
	for _, offer := range mapper {
		if offer.AccountId == id {
			return &offer, nil
		}
	}
	return nil, errors.NotFoundError
}
