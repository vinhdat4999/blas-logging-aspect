package com.blas.blasloggingaspect.context.impl;

import static com.blas.blascommon.constants.MdcConstants.CALLER_ID;
import static com.blas.blascommon.constants.MdcConstants.CALLER_SERVICE_NAME;
import static com.blas.blascommon.constants.MdcConstants.GLOBAL_ID;
import static com.blas.blascommon.constants.MdcConstants.LOCAL_ID;
import static com.blas.blascommon.constants.MdcConstants.TRANSACTION_ID;
import static com.blas.blasloggingaspect.constant.LayoutConstants.SPAN_ID_FIELD;
import static com.blas.blasloggingaspect.constant.LayoutConstants.TRACE_ID_FIELD;

import com.blas.blasloggingaspect.context.MdcProvider;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class MdcProviderImpl implements MdcProvider {

  @Override
  public void clear() {
    MDC.clear();
  }

  @Override
  public void put(String key, String value) {
    MDC.put(key, value);
  }

  @Override
  public String get(String key) {
    return MDC.get(key);
  }

  @Override
  public void remove(String key) {
    MDC.remove(key);
  }

  @Override
  public String getTraceId() {
    return MDC.get(TRACE_ID_FIELD);
  }

  @Override
  public String getSpanId() {
    return MDC.get(SPAN_ID_FIELD);
  }

  @Override
  public String getGlobalId() {
    return MDC.get(GLOBAL_ID);
  }

  @Override
  public String getLocalId() {
    return MDC.get(LOCAL_ID);
  }

  @Override
  public String getCallerId() {
    return MDC.get(CALLER_ID);
  }

  @Override
  public String getCallerServiceId() {
    return MDC.get(CALLER_SERVICE_NAME);
  }

  @Override
  public String getTransactionId() {
    return MDC.get(TRANSACTION_ID);
  }
}
