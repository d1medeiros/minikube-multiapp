package service

import (
	"api-customer/internal/model"
	"errors"
)

var mapper = map[string]model.Customer{
	"1": {Id: "1", Name: "diego", Document: "1234"},
	"2": {Id: "2", Name: "natalia", Document: "4321"},
}

func GetCustomers(id string) (*model.Customer, error) {
	c, isOk := mapper[id]
	if !isOk {
		return nil, errors.New("not found")
	}
	return &c, nil
}

func GetCustomersAll() []model.Customer {
	var listC []model.Customer
	for _, c := range mapper {
		listC = append(listC, c)
	}
	return listC
}
