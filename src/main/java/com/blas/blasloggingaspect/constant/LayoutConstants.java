package com.blas.blasloggingaspect.constant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LayoutConstants {

  private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
  public static final DateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(TIME_FORMAT);
  public static final String SERVICE_NAME_FIELD = "ServiceName";
  public static final String TIME_STAMP_FIELD = "Timestamp";
  public static final String TRACE_ID_FIELD = "TraceId";
  public static final String SPAN_ID_FIELD = "SpanId";
  public static final String SEVERITY_TEXT_FIELD = "SeverityText";
  public static final String SEVERITY_NUMBER_FIELD = "SeverityNumber";
  public static final String K8S_NAMESPACE_FIELD = "K8sNamespace";
  public static final String RESPONSE_TIME_FIELD = "ResponseTime";
  public static final String METHOD_SIGNATURE_FIELD = "MethodSignature";
  public static final String APP_METHOD_FIELD = "Method";
  public static final String PROTOCOL_FIELD = "Protocol";
  public static final String STATUS_CODE = "StatusCode";

  public static final String RESOURCE_FIELD = "Resource";
  public static final String BODY_FIELD = "Body";
  public static final String ATTRIBUTES_FIELD = "Attributes";
  public static final String UNKNOWN = "unknown";
}
