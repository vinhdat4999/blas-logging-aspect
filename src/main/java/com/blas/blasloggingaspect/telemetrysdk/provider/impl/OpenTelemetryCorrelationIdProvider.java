package com.blas.blasloggingaspect.telemetrysdk.provider.impl;

import com.blas.blasloggingaspect.telemetrysdk.provider.ChainTelemetryCorrelationProvider;
import com.blas.blasloggingaspect.telemetrysdk.provider.TelemetryCorrelationIdProvider;
import com.blas.blasloggingaspect.telemetrysdk.provider.TelemetryCorrelationIds;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import java.util.Optional;

public class OpenTelemetryCorrelationIdProvider extends ChainTelemetryCorrelationProvider {

  public OpenTelemetryCorrelationIdProvider(TelemetryCorrelationIdProvider nextProvider) {
    super(nextProvider);
  }

  @Override
  protected Optional<TelemetryCorrelationIds> getIdInternal() {
    Span span = Span.current();
    SpanContext spanContext = span.getSpanContext();
    if (spanContext.isValid()) {
      return Optional.of(
          OpenTelemetryCorrelationIds.builder()
              .traceId(spanContext.getTraceId())
              .spanId(spanContext.getSpanId())
              .build()
      );
    } else {
      return Optional.empty();
    }
  }
}
