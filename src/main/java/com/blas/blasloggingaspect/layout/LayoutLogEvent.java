package com.blas.blasloggingaspect.layout;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LayoutLogEvent {

  private String level;
  private String message;
  private Long timeMillis;
  private String stackTrace;
  private String loggerName;
  private String k8sNamespace;

  public int getLevelAsNumber() {
    return switch (this.level) {
      case "TRACE" -> 1;
      case "DEBUG" -> 5;
      case "INFO" -> 9;
      case "WARN" -> 13;
      case "ERROR" -> 17;
      case "FATAL" -> 21;
      default -> 0;
    };
  }
}
