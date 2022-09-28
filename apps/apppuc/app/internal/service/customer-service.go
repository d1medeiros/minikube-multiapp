package service

import (
	"apppuc/internal/model/customer"
	"errors"
)

var mapper = map[string]customer.Customer{
	"x1": {Id: "x1", Eligible: false},
	"x2": {Id: "x2", Eligible: true, Limit: 50000.0},
	"x3": {Id: "x3", Eligible: true, Limit: 150000.0},
}

func GetCustomers(id string) (*customer.Customer, error) {
	c, isOk := mapper[id]
	if !isOk {
		return nil, errors.New("not found")
	}
	return &c, nil
}
