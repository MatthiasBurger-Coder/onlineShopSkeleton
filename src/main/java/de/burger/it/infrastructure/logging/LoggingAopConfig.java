package de.burger.it.infrastructure.logging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class LoggingAopConfig {
    @Bean
    public MethodLoggingAspect methodLoggingAspect() {
        return new MethodLoggingAspect();
    }
}
