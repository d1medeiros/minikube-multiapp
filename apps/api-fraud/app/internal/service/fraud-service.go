package service

import (
	"api-fraud/internal/model"
	"context"
	"mylibs/pkg/observability/motel"
)

var mapper = map[string]model.Fraud{
	"abc": {AccountId: "abc", Allowed: false},
	"cba": {AccountId: "cba", Allowed: true},
}

func Verify(ctx context.Context, tr motel.MyTracer, id string) bool {
	_, span := tr.Start(ctx, "Verify")
	defer span.End()

	c, isOk := mapper[id]
	if !isOk {
		return false
	}
	return c.Allowed
}
