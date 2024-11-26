package com.blas.blasloggingaspect.telemetrysdk.provider;

import static com.blas.blasloggingaspect.telemetrysdk.provider.impl.OpenTelemetryCorrelationIds.EMPTY_ID;

import java.util.Optional;

public abstract class ChainTelemetryCorrelationProvider implements TelemetryCorrelationIdProvider {

  private final TelemetryCorrelationIdProvider nextProvider;

  protected ChainTelemetryCorrelationProvider(TelemetryCorrelationIdProvider nextProvider) {
    this.nextProvider = nextProvider;
  }

  protected abstract Optional<TelemetryCorrelationIds> getIdInternal();

  @Override
  public TelemetryCorrelationIds getId() {
    Optional<TelemetryCorrelationIds> id = getIdInternal();
    if (id.isEmpty() && nextProvider != null) {
      return nextProvider.getId();
    }
    return id.orElse(EMPTY_ID);
  }
}
