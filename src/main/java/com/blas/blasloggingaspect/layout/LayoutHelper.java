package com.blas.blasloggingaspect.layout;

import static com.blas.blascommon.constants.MdcConstants.CALLER_ID;
import static com.blas.blascommon.constants.MdcConstants.CALLER_SERVICE_NAME;
import static com.blas.blascommon.constants.MdcConstants.GLOBAL_ID;
import static com.blas.blascommon.constants.MdcConstants.LOCAL_ID;
import static com.blas.blasloggingaspect.constant.LayoutConstants.ATTRIBUTES_FIELD;
import static com.blas.blasloggingaspect.constant.LayoutConstants.BODY_FIELD;
import static com.blas.blasloggingaspect.constant.LayoutConstants.K8S_NAMESPACE_FIELD;
import static com.blas.blasloggingaspect.constant.LayoutConstants.RESOURCE_FIELD;
import static com.blas.blasloggingaspect.constant.LayoutConstants.SERVICE_NAME_FIELD;
import static com.blas.blasloggingaspect.constant.LayoutConstants.SEVERITY_NUMBER_FIELD;
import static com.blas.blasloggingaspect.constant.LayoutConstants.SEVERITY_TEXT_FIELD;
import static com.blas.blasloggingaspect.constant.LayoutConstants.SIMPLE_DATE_FORMAT;
import static com.blas.blasloggingaspect.constant.LayoutConstants.SPAN_ID_FIELD;
import static com.blas.blasloggingaspect.constant.LayoutConstants.TIME_STAMP_FIELD;
import static com.blas.blasloggingaspect.constant.LayoutConstants.TRACE_ID_FIELD;
import static com.blas.blasloggingaspect.listener.CustomApplicationListener.getApplicationName;
import static org.apache.commons.lang3.StringUtils.SPACE;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;
import lombok.experimental.UtilityClass;
import org.slf4j.MDC;

@UtilityClass
public class LayoutHelper {

  public static Map<String, Object> serialize(LayoutLogEvent event) {
    LinkedHashMap<String, Object> map = new LinkedHashMap<>();

    map.put(SERVICE_NAME_FIELD, getApplicationName());
    map.put(TIME_STAMP_FIELD, event.getTimeMillis());
    map.put(TRACE_ID_FIELD, MDC.get(TRACE_ID_FIELD));
    map.put(SPAN_ID_FIELD, MDC.get(SPAN_ID_FIELD));
    map.put(SEVERITY_TEXT_FIELD, event.getLevel());
    map.put(SEVERITY_NUMBER_FIELD, event.getLevelAsNumber());

    Map<String, String> resourceMap = new HashMap<>();
    resourceMap.put(K8S_NAMESPACE_FIELD, event.getK8sNamespace());
    map.put(RESOURCE_FIELD, resourceMap);

    Map<String, String> attributeMap = new HashMap<>();
    attributeMap.put(GLOBAL_ID, MDC.get(GLOBAL_ID));
    attributeMap.put(LOCAL_ID, MDC.get(LOCAL_ID));
    attributeMap.put(CALLER_ID, MDC.get(CALLER_ID));
    attributeMap.put(CALLER_SERVICE_NAME, MDC.get(CALLER_SERVICE_NAME));
    map.put(ATTRIBUTES_FIELD, attributeMap);

    StringJoiner message = new StringJoiner(SPACE);
    message.add(SIMPLE_DATE_FORMAT.format(new Date(event.getTimeMillis())));
    message.add(event.getLevel());
    message.add(event.getLoggerName());
    message.add(event.getMessage());

    // Exceptions
    if (event.getStackTrace() != null) {
      final String stackTrace = event.getStackTrace();
      message.add(stackTrace);
    }
    map.put(BODY_FIELD, message.toString());
    return map;
  }
}
