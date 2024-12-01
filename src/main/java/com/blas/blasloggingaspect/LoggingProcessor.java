package com.blas.blasloggingaspect;

import static com.blas.blascommon.constants.IdHttpHeader.CALLER_ID;
import static com.blas.blascommon.constants.IdHttpHeader.CALLER_SERVICE_ID;
import static com.blas.blascommon.constants.IdHttpHeader.GLOBAL_ID;
import static com.blas.blascommon.constants.MdcConstants.CALLER_SERVICE_NAME;
import static com.blas.blascommon.constants.MdcConstants.LOCAL_ID;
import static com.blas.blascommon.utils.idutils.IdUtils.genUniqueId;
import static com.blas.blasloggingaspect.constant.LayoutConstants.APP_METHOD_FIELD;
import static com.blas.blasloggingaspect.constant.LayoutConstants.METHOD_SIGNATURE_FIELD;
import static com.blas.blasloggingaspect.constant.LayoutConstants.PROTOCOL_FIELD;
import static com.blas.blasloggingaspect.constant.LayoutConstants.RESPONSE_TIME_FIELD;
import static com.blas.blasloggingaspect.constant.LayoutConstants.SPAN_ID_FIELD;
import static com.blas.blasloggingaspect.constant.LayoutConstants.STATUS_CODE;
import static com.blas.blasloggingaspect.constant.LayoutConstants.TRACE_ID_FIELD;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isBlank;

import com.blas.blascommon.constants.MdcConstants;
import com.blas.blasloggingaspect.context.MdcProvider;
import com.blas.blasloggingaspect.telemetrysdk.provider.TelemetryCorrelationIdProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingProcessor {

  private final HttpServletRequest request;
  private final MdcProvider mdcProvider;
  private final ObjectMapper objectMapper;
  private final TelemetryCorrelationIdProvider telemetryCorrelationIdProvider;

  @Pointcut("within(@org.springframework.stereotype.Controller *) || within(@org.springframework.web.bind.annotation.RestController *)")
  public void controller() {
  }

  @Pointcut("execution(* com.blas.blascommon.configurations.MaintenanceConfiguration.checkMaintenance(..))")
  public void checkMaintenanceMethod() {
  }

  @Pointcut("execution(* com.blas.blascommon.configurations.EmailQueueService.sendMessage(..))")
  public void hazelcastMessageSenderMethod() {
  }

  @Around("checkMaintenanceMethod()")
  public Object logAroundMaintenanceCheck(ProceedingJoinPoint joinPoint) throws Throwable {
    preProcessorHttpRequest();
    return joinPoint.proceed();
  }

  @Around("hazelcastMessageSenderMethod()")
  public Object logAroundHazelcastMessageSenderMethod(ProceedingJoinPoint joinPoint)
      throws Throwable {
    preProcessorHazelcast();
    return joinPoint.proceed();
  }

  @Around("controller()")
  public Object logAroundController(ProceedingJoinPoint joinPoint) throws Throwable {

    final long startTime = System.currentTimeMillis();
    Object result = joinPoint.proceed();
    final long responseTime = System.currentTimeMillis() - startTime;

    return postProcessor(joinPoint, result, responseTime);
  }

  private void preProcessorHazelcast() {
    String globalId = mdcProvider.getGlobalId();
    String callerId = mdcProvider.getCallerId();
    String callerServiceName = mdcProvider.getCallerServiceId();

    mdcProvider.put(TRACE_ID_FIELD, telemetryCorrelationIdProvider.getId().getTraceId());
    mdcProvider.put(SPAN_ID_FIELD, telemetryCorrelationIdProvider.getId().getSpanId());

    updateMdcStack(globalId, callerId, callerServiceName);
  }

  private void preProcessorHttpRequest() {
    String globalId = mdcProvider.getGlobalId();
    String callerId = mdcProvider.getCallerId();
    String callerServiceName = mdcProvider.getCallerServiceId();

    if (null != request) {
      Enumeration<String> headerNames = request.getHeaderNames();
      while (headerNames.hasMoreElements()) {
        String headerName = headerNames.nextElement();
        String headerValue = request.getHeader(headerName);
        if (equalsIgnoreCase(GLOBAL_ID, headerName)) {
          globalId = headerValue;
        } else if (equalsIgnoreCase(CALLER_ID, headerName)) {
          callerId = headerValue;
        } else if (equalsIgnoreCase(CALLER_SERVICE_ID, headerName)) {
          callerServiceName = headerValue;
        }
      }
    }

    mdcProvider.put(TRACE_ID_FIELD, telemetryCorrelationIdProvider.getId().getTraceId());
    mdcProvider.put(SPAN_ID_FIELD, telemetryCorrelationIdProvider.getId().getSpanId());

    updateMdcStack(globalId, callerId, callerServiceName);
  }

  private Object postProcessor(ProceedingJoinPoint joinPoint, Object result, long responseTime)
      throws JsonProcessingException {
    mdcProvider.put(RESPONSE_TIME_FIELD, String.valueOf(responseTime));
    mdcProvider.put(METHOD_SIGNATURE_FIELD, joinPoint.getStaticPart().getSignature().toString());

    if (result instanceof ResponseEntity<?> responseEntity) {
      mdcProvider.put(STATUS_CODE, responseEntity.getStatusCode().toString());
    }

    logResponse();
    return result;
  }

  private void logResponse() throws JsonProcessingException {
    Map<String, Object> response = new HashMap<>();
    response.put(RESPONSE_TIME_FIELD, mdcProvider.get(RESPONSE_TIME_FIELD));
    response.put(METHOD_SIGNATURE_FIELD, mdcProvider.get(METHOD_SIGNATURE_FIELD));
    response.put(APP_METHOD_FIELD, mdcProvider.get(APP_METHOD_FIELD));
    response.put(PROTOCOL_FIELD, mdcProvider.get(PROTOCOL_FIELD));
    response.put(STATUS_CODE, mdcProvider.get(STATUS_CODE));

    if (log.isInfoEnabled()) {
      log.info(objectMapper.writeValueAsString(response));
    }
  }

  private void updateMdcStack(String globalId, String callerId, String callerName) {
    if (isBlank(globalId)) {
      globalId = genUniqueId();
    }

    mdcProvider.put(MdcConstants.GLOBAL_ID, globalId);
    mdcProvider.put(MdcConstants.CALLER_ID, callerId);
    mdcProvider.put(LOCAL_ID, genUniqueId());
    mdcProvider.put(CALLER_SERVICE_NAME, callerName);
    if (null != request) {
      try {
        mdcProvider.put(APP_METHOD_FIELD, request.getMethod());
        mdcProvider.put(PROTOCOL_FIELD, request.getProtocol());
      } catch (Exception ignored) {
        // no action
      }
    }
  }
}
