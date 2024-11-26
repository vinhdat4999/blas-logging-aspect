package com.blas.blasloggingaspect.listener;

import lombok.NonNull;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

public class CustomApplicationListener implements ApplicationListener<ApplicationEvent> {

  private static String applicationName;
  private static String k8sNamespace;

  @Override
  public void onApplicationEvent(@NonNull ApplicationEvent event) {
    if (event instanceof ApplicationContextInitializedEvent applicationContextInitializedEvent) {
      ApplicationContext context = applicationContextInitializedEvent.getApplicationContext();
      applicationName = context.getEnvironment().getProperty("blas.service.serviceName");
      k8sNamespace = context.getEnvironment().getProperty("blas.hazelcast.aksNamespace");
    }
  }

  public static String getApplicationName() {
    return applicationName;
  }

  public static String getK8sNamespace() {
    return k8sNamespace;
  }
}
