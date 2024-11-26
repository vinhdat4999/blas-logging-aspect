package com.blas.blasloggingaspect.telemetrysdk.provider;

public interface TelemetryCorrelationIds {

  String getTraceId();

  String getSpanId();

}
