package mhttp

import (
	"go.opentelemetry.io/otel"
	"go.opentelemetry.io/otel/attribute"
	"go.opentelemetry.io/otel/exporters/jaeger"
	"go.opentelemetry.io/otel/propagation"
	"go.opentelemetry.io/otel/sdk/resource"
	tracesdk "go.opentelemetry.io/otel/sdk/trace"
	semconv "go.opentelemetry.io/otel/semconv/v1.18.0"
	"go.opentelemetry.io/otel/trace"
)

type OTELWrapper struct {
	tp trace.TracerProvider
}

func (o *OTELWrapper) Init(tp *tracesdk.TracerProvider) {
	otel.SetTracerProvider(tp)
	otel.SetTextMapPropagator(propagation.TraceContext{})
}

func (o *OTELWrapper) TracerProvider(service string, url string) error {
	// Create the Jaeger exporter
	exp, err := jaeger.New(jaeger.WithCollectorEndpoint(jaeger.WithEndpoint(url)))
	if err != nil {
		return err
	}
	o.tp = tracesdk.NewTracerProvider(
		tracesdk.WithBatcher(exp),
		tracesdk.WithResource(resource.NewWithAttributes(
			semconv.SchemaURL,
			semconv.ServiceName(service),
			attribute.String("environment", "env"),
			attribute.Int64("ID", 1),
		)),
	)
	otel.SetTracerProvider(o.tp)
	otel.SetTextMapPropagator(propagation.TraceContext{})

	return nil
}

func (o *OTELWrapper) Tracer(name string, opts ...trace.TracerOption) trace.Tracer {
	return o.tp.Tracer(name, opts...)
}
