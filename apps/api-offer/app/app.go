package main

import (
	"api-offer/internal/application/service"
	"encoding/json"
	"fmt"
	"github.com/rs/zerolog"
	"github.com/rs/zerolog/log"
	"mylibs/pkg/observability/motel"
	"net/http"
	"os"
)

var traceEndpoint = os.Getenv("TRACE_ENDPOINT")
var appname = os.Getenv("APP_NAME")

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
		q := r.URL.Query()
		id := q.Get("account_id")
		ow.GetTextMapPropagatorExtractor(r.Context(), r.Header)
		spanName := fmt.Sprintf("%s %s", r.Method, r.URL.Path)
		tr := ow.Tracer("offer-tracer")
		ctx, span := tr.Start(r.Context(), spanName)
		defer span.End()

		span.SetAttributes("tid", tid)
		log.Info().Str("tid", tid).Msg("finding offers")
		o, err := service.GetOffers(ctx, tr, id)

		marshal, err := json.Marshal(o)
		if err != nil {
			w.WriteHeader(http.StatusInternalServerError)
			w.Write([]byte("500 - Something bad happened!"))
		} else {
			w.WriteHeader(http.StatusOK)
			w.Write(marshal)
		}
	}
	handler := http.HandlerFunc(httpHandler)
	wrappedHandler := motel.NewHandler(handler, "offer-instrumented")
	http.Handle("/offers", wrappedHandler)
	err = http.ListenAndServe(":3000", nil)
	panic(err)
}
