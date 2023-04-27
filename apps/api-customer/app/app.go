package main

import (
	"api-customer/internal/service"
	"encoding/json"
	"fmt"
	"github.com/rs/zerolog"
	"github.com/rs/zerolog/log"
	"go.opentelemetry.io/contrib/instrumentation/net/http/otelhttp"
	"go.opentelemetry.io/otel"
	"go.opentelemetry.io/otel/attribute"
	"go.opentelemetry.io/otel/exporters/jaeger"
	"go.opentelemetry.io/otel/exporters/stdout/stdouttrace"
	"go.opentelemetry.io/otel/propagation"
	"go.opentelemetry.io/otel/sdk/resource"
	tracesdk "go.opentelemetry.io/otel/sdk/trace"
	semconv "go.opentelemetry.io/otel/semconv/v1.17.0"
	"net/http"
	"os"
)

var appname = os.Getenv("APP_NAME")

func tracerProvider(service string, url string) (*tracesdk.TracerProvider, error) {
	// Create the Jaeger exporter
	ex, err := jaeger.New(jaeger.WithCollectorEndpoint(jaeger.WithEndpoint(url)))
	if err != nil {
		return nil, err
	}
	exp, err := stdouttrace.New(stdouttrace.WithPrettyPrint())
	bsp := tracesdk.NewBatchSpanProcessor(exp)
	tp := tracesdk.NewTracerProvider(
		// Always be sure to batch in production.
		tracesdk.WithSampler(tracesdk.AlwaysSample()),
		tracesdk.WithBatcher(ex),
		tracesdk.WithSpanProcessor(bsp),
		// Record information about this application in a Resource.
		tracesdk.WithResource(resource.NewWithAttributes(
			semconv.SchemaURL,
			semconv.ServiceName(service),
			attribute.String("environment", "env"),
			attribute.Int64("ID", 1),
		)),
	)
	return tp, nil
}

func main() {

	//logs
	zerolog.TimestampFieldName = "date"
	zerolog.ErrorFieldName = "message"

	log.Logger = log.With().Str("application", appname).Logger()

	//trace
	TP, err := tracerProvider(appname, "http://simplest-collector.monitoring:14268/api/traces")
	if err != nil {
		panic(err)
	}
	// Register our TracerProvider as the global so any imported
	// instrumentation in the future will default to using it.
	otel.SetTracerProvider(TP)
	otel.SetTextMapPropagator(propagation.TraceContext{})
	httpHandler := func(w http.ResponseWriter, r *http.Request) {
		for name, values := range r.Header {
			for _, value := range values {
				log.Info().Msgf("%s %s", name, value)
			}
		}
		tid := r.Header.Get("tid")
		otel.GetTextMapPropagator().Extract(r.Context(), propagation.HeaderCarrier(r.Header))
		spanName := fmt.Sprintf("%s %s", r.Method, r.URL.Path)
		tr := TP.Tracer("customer-tracer")
		ctx, span := tr.Start(r.Context(), spanName)
		defer span.End()

		span.SetAttributes(attribute.Key("tid").String(tid))
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
	// Wrap your httpHandler function.
	handler := http.HandlerFunc(httpHandler)
	wrappedHandler := otelhttp.NewHandler(handler, "customer-instrumented")
	http.Handle("/customers", wrappedHandler)
	// And start the HTTP serve.
	err = http.ListenAndServe(":3000", nil)
	panic(err)
}
