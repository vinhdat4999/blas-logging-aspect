package com.blas.blasloggingaspect.config;

import com.blas.blasloggingaspect.telemetrysdk.provider.TelemetryCorrelationIdProvider;
import com.blas.blasloggingaspect.telemetrysdk.provider.impl.OpenTelemetryCorrelationIdProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.blas.blasloggingaspect")
public class TracerExporterConfiguration {

  @Bean
  public TelemetryCorrelationIdProvider telemetryCorrelationIdProvider() {
    return new OpenTelemetryCorrelationIdProvider(null);
  }
}
