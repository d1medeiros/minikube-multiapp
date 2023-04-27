package service

import (
	"api-customer/internal/model"
	"context"
	"errors"
	"mylibs/pkg/observability/motel"
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

func GetCustomersAll(ctx context.Context, tr motel.MyTracer) []model.Customer {
	_, span := tr.Start(ctx, "GetCustomersAll")
	defer span.End()

	var listC []model.Customer
	for _, c := range mapper {
		listC = append(listC, c)
	}
	return listC
}
