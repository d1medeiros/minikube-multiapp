package service

import (
	"api-account/internal/model"
	"errors"
)

var mapper = map[string]model.Account{
	"1": {Id: "abc", CustomerId: "1"},
	"2": {Id: "cba", CustomerId: "2"},
}

func GetAccount(id string) (*model.Account, error) {
	c, isOk := mapper[id]
	if !isOk {
		return nil, errors.New("not found")
	}
	return &c, nil
}
