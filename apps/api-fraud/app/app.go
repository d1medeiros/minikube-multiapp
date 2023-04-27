package main

import (
	"api-fraud/internal/service"
	"fmt"
	"github.com/rs/zerolog"
	"github.com/rs/zerolog/log"
	"mylibs/pkg/observability/motel"
	"net/http"
	"os"
	"strings"
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
		tr := ow.Tracer("fraud-tracer")
		ctx, span := tr.Start(r.Context(), spanName)
		defer span.End()

		span.SetAttributes("tid", tid)
		name := strings.TrimPrefix(r.URL.Path, "/frauds/")
		isAllowed := service.Verify(ctx, tr, name)
		log.Info().Str("tid", tid).Msg("finding frauds")

		if err != nil {
			w.WriteHeader(http.StatusInternalServerError)
			w.Write([]byte(err.Error()))
		} else if !isAllowed {
			w.WriteHeader(http.StatusForbidden)
			w.Write([]byte("403 - Something bad happened!"))
		} else {
			w.WriteHeader(http.StatusOK)
			w.Write([]byte("{}"))
		}
	}
	handler := http.HandlerFunc(httpHandler)
	wrappedHandler := motel.NewHandler(handler, "fraud-instrumented")
	http.Handle("/frauds/", wrappedHandler)
	err = http.ListenAndServe(":3000", nil)
	panic(err)

	//app := fiber.New(fiber.Config{
	//	DisableStartupMessage: true,
	//})
	//zerolog.TimestampFieldName = "date"
	//zerolog.ErrorFieldName = "message"
	//var appname = os.Getenv("APP_NAME")
	//log.Logger = log.With().Str("application", appname).Logger()
	//prometheus := fiberprometheus.New(appname)
	//prometheus.RegisterAt(app, "/metrics")
	//app.Use(prometheus.Middleware)
	//
	//app.Get("/frauds/:id", func(c *fiber.Ctx) error {
	//	c.Accepts("application/json")
	//	name := c.Params("id", "0")
	//	tid := c.Get("tid", "-")
	//	log.Info().Str("tid", tid).Msg(name)
	//	isAllowed := service.Verify(name)
	//	if !isAllowed {
	//		return fiber.NewError(403, "nao permitido")
	//	}
	//	return c.JSON(`{"allowed": true}`)
	//})
	//
	//err := app.Listen(":3000")
	//if err != nil {
	//	log.Error().Err(err).Msg("")
	//}
}
