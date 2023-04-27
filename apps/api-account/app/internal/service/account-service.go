package service

import (
	"api-account/internal/model"
	"context"
	"errors"
	"mylibs/pkg/observability/motel"
)

var mapper = map[string]model.Account{
	"1": {Id: "abc", CustomerId: "1"},
	"2": {Id: "cba", CustomerId: "2"},
}

func GetAccount(ctx context.Context, tr motel.MyTracer, id string) (*model.Account, error) {
	_, span := tr.Start(ctx, "GetAccount")
	defer span.End()

	c, isOk := mapper[id]
	if !isOk {
		return nil, errors.New("not found")
	}
	return &c, nil
}
