package de.burger.it.infrastructure.logging

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.core.config.LoggerConfig
import org.apache.logging.log4j.core.test.appender.ListAppender
import org.slf4j.MDC
import org.springframework.aop.support.AopUtils
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import spock.lang.Specification

class MethodLoggingAspectErrorSpec extends Specification {

    def "AfterThrowing: error logs with stacktrace and correlation context"() {
        given: "a Spring context with the logging aspect and a failing target bean"
        def ctx = new AnnotationConfigApplicationContext()
        ctx.register(LoggingAopConfig, FailingService)
        ctx.refresh()
        def svc = ctx.getBean(FailingService)

        and: "a ListAppender attached to the exact target class logger"
        def lc  = (LoggerContext) LogManager.getContext(false)
        def cfg = lc.configuration

        // Resolve the real target class behind a proxy (CGLIB/JDK)
        def targetClass = AopUtils.getTargetClass(svc)
        def targetLoggerName = targetClass.name

        def app = new ListAppender("mem"); app.start()

        // Create a dedicated LoggerConfig for the target class and attach the appender
        def loggerCfgExact = new LoggerConfig(targetLoggerName, Level.INFO, true) // additive = true
        loggerCfgExact.addAppender(app, Level.INFO, null)
        cfg.addLogger(targetLoggerName, loggerCfgExact)
        lc.updateLoggers()

        when: "the target method throws"
        svc.explode("boom")

        then: "the exception is propagated"
        thrown(IllegalStateException)

        and: "correlation id is present in MDC"
        MDC.get("cid") != null

        and: "an ERROR event with throwable and cid is captured"
        app.events.stream().anyMatch {
            it.level == Level.ERROR &&
                    it.thrown != null &&
                    it.message.formattedMessage.contains("failed") &&
                    it.contextData.toMap().containsKey("cid")
        }

        cleanup:
        ctx.close()
    }

    static class FailingService {
        String explode(String input) {
            throw new IllegalStateException("kaboom: $input")
        }
    }
}
