package com.blas.blasloggingaspect.layout;

import static com.blas.blasloggingaspect.constant.LayoutConstants.UNKNOWN;
import static com.blas.blasloggingaspect.layout.LayoutHelper.serialize;
import static com.blas.blasloggingaspect.listener.CustomApplicationListener.getK8sNamespace;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.contrib.jackson.JacksonJsonFormatter;
import ch.qos.logback.contrib.json.JsonLayoutBase;
import java.util.Map;
import java.util.Optional;

public class LogbackJsonLayout extends JsonLayoutBase<ILoggingEvent> {

  public LogbackJsonLayout() {
    super();
    this.setJsonFormatter(new JacksonJsonFormatter());
    this.setAppendLineSeparator(true);
  }

  @Override
  protected Map<String, Object> toJsonMap(ILoggingEvent iLoggingEvent) {
    String level = iLoggingEvent.getLevel().toString();
    String message = iLoggingEvent.getFormattedMessage();
    Long timeMillis = iLoggingEvent.getTimeStamp();
    String stackTrace = Optional.ofNullable(iLoggingEvent.getThrowableProxy())
        .map(IThrowableProxy::toString)
        .orElse(null);
    String loggerName = iLoggingEvent.getLoggerName();
    String k8sNamespace = defaultIfBlank(getK8sNamespace(), UNKNOWN);
    LayoutLogEvent layoutLogEvent = new LayoutLogEvent(level, message, timeMillis, stackTrace,
        loggerName, k8sNamespace);
    return serialize(layoutLogEvent);
  }
}
