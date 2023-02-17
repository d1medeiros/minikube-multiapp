package service

import (
	"api-fraud/internal/model"
)

var mapper = map[string]model.Fraud{
	"abc": {AccountId: "abc", Allowed: false},
	"cba": {AccountId: "cba", Allowed: true},
}

func Verify(id string) bool {
	c, isOk := mapper[id]
	if !isOk {
		return false
	}
	return c.Allowed
}
