package main

import (
	"api-customer/internal/service"
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

	//logs
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
		tr := ow.Tracer("customer-tracer")
		ctx, span := tr.Start(r.Context(), spanName)
		defer span.End()

		span.SetAttributes("tid", tid)
		lc := service.GetCustomersAll(ctx, tr)
		log.Info().Str("tid", tid).Msg("finding all customers")
		marshal, err := json.Marshal(lc)
		if err != nil {
			w.WriteHeader(http.StatusInternalServerError)
			w.Write([]byte("500 - Something bad happened!"))
		} else {
			w.WriteHeader(http.StatusOK)
			w.Write(marshal)
		}
	}
	handler := http.HandlerFunc(httpHandler)
	wrappedHandler := motel.NewHandler(handler, "customer-instrumented")
	http.Handle("/customers", wrappedHandler)
	err = http.ListenAndServe(":3000", nil)
	panic(err)
}
