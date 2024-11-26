package com.blas.blasloggingaspect.telemetrysdk.provider.impl;

import com.blas.blasloggingaspect.telemetrysdk.provider.TelemetryCorrelationIds;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class OpenTelemetryCorrelationIds implements TelemetryCorrelationIds {

  public static final OpenTelemetryCorrelationIds EMPTY_ID = new OpenTelemetryCorrelationIds(null,
      null);

  private String traceId;
  private String spanId;

  @Override
  public String getTraceId() {
    return traceId;
  }

  @Override
  public String getSpanId() {
    return spanId;
  }
}
