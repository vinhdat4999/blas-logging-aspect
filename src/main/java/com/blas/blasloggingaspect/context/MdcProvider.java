package com.blas.blasloggingaspect.context;

public interface MdcProvider {

  void clear();

  void put(String key, String value);

  String get(String key);

  void remove(String key);

  String getTraceId();

  String getSpanId();

  String getGlobalId();

  String getLocalId();

  String getCallerId();

  String getCallerServiceId();

  String getTransactionId();
}
