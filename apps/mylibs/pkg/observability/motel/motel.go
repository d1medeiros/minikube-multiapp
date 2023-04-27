package motel

import (
	"context"
	"go.opentelemetry.io/contrib/instrumentation/net/http/otelhttp"
	"go.opentelemetry.io/otel"
	"go.opentelemetry.io/otel/attribute"
	"go.opentelemetry.io/otel/exporters/jaeger"
	"go.opentelemetry.io/otel/exporters/stdout/stdouttrace"
	"go.opentelemetry.io/otel/propagation"
	"go.opentelemetry.io/otel/sdk/resource"
	tracesdk "go.opentelemetry.io/otel/sdk/trace"
	semconv "go.opentelemetry.io/otel/semconv/v1.18.0"
	"go.opentelemetry.io/otel/trace"
	"net/http"
)

type OTELWrapper struct {
	tp trace.TracerProvider
}

func (o *OTELWrapper) TracerProvider2(service string, url string) error {
	// Create the Jaeger exporter
	ex, err := jaeger.New(jaeger.WithCollectorEndpoint(jaeger.WithEndpoint(url)))
	if err != nil {
		return err
	}
	exp, err := stdouttrace.New(stdouttrace.WithPrettyPrint())
	bsp := tracesdk.NewBatchSpanProcessor(exp)
	o.tp = tracesdk.NewTracerProvider(
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
	otel.SetTracerProvider(o.tp)
	otel.SetTextMapPropagator(propagation.TraceContext{})
	return nil
}

func (o *OTELWrapper) TracerProvider(service string, url string) error {
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

func (o *OTELWrapper) Tracer(name string, opts ...trace.TracerOption) MyTracer {
	return &myTracer{
		t: o.tp.Tracer(name, opts...),
	}
}

func (o *OTELWrapper) GetTextMapPropagatorExtractor(ctx context.Context, header http.Header) {
	otel.GetTextMapPropagator().Extract(ctx, propagation.HeaderCarrier(header))
}

type MyTracer interface {
	Start(
		ctx context.Context,
		spanName string,
	) (context.Context, MySpan)
}

type myTracer struct {
	t trace.Tracer
}

func (o *myTracer) Start(
	ctx context.Context,
	spanName string,
) (context.Context, MySpan) {
	start, span := o.t.Start(ctx, spanName)
	return start, &mySpan{
		s: span,
	}
}

type MySpan interface {
	End()
	SetAttributes(key string, value string)
}

type mySpan struct {
	s trace.Span
}

func (s *mySpan) End() {
	s.s.End()
}

func (s *mySpan) SetAttributes(key string, value string) {
	s.s.SetAttributes(attribute.Key(key).String(value))
}

func NewClient() http.Client {
	return http.Client{
		Transport: otelhttp.NewTransport(http.DefaultTransport),
	}
}

func NewHandler(handler http.Handler, operation string) http.Handler {
	return otelhttp.NewHandler(handler, operation)
}
