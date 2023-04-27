package main

import (
	"api-account/internal/service"
	"encoding/json"
	"fmt"
	"github.com/rs/zerolog"
	"github.com/rs/zerolog/log"
	"mylibs/pkg/observability/motel"
	"net/http"
	"os"
)

var appname = os.Getenv("APP_NAME")
var traceEndpoint = os.Getenv("TRACE_ENDPOINT")

func main() {
	zerolog.TimestampFieldName = "date"
	zerolog.ErrorFieldName = "message"
	log.Logger = log.With().Str("application", appname).Logger()

	//trace
	ow := motel.OTELWrapper{}
	err := ow.TracerProvider2(appname, traceEndpoint)
	if err != nil {
		panic(err)
	}
	httpHandler := func(w http.ResponseWriter, r *http.Request) {
		tid := r.Header.Get("tid")
		ow.GetTextMapPropagatorExtractor(r.Context(), r.Header)
		spanName := fmt.Sprintf("%s %s", r.Method, r.URL.Path)
		tr := ow.Tracer("account-tracer")
		ctx, span := tr.Start(r.Context(), spanName)
		defer span.End()

		span.SetAttributes("tid", tid)
		q := r.URL.Query()
		name := q.Get("customer_id")
		account, err := service.GetAccount(ctx, tr, name)
		log.Info().Str("tid", tid).Msg("finding accounts")
		marshal, err := json.Marshal(account)
		if err != nil {
			w.WriteHeader(http.StatusInternalServerError)
			w.Write([]byte("500 - Something bad happened!"))
		} else {
			w.WriteHeader(http.StatusOK)
			w.Write(marshal)
		}
	}
	handler := http.HandlerFunc(httpHandler)
	wrappedHandler := motel.NewHandler(handler, "account-instrumented")
	http.Handle("/accounts", wrappedHandler)
	err = http.ListenAndServe(":3000", nil)
	panic(err)
}
