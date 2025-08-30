package de.burger.it.infrastructure.logging

import groovy.util.logging.Log4j2
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.core.test.appender.ListAppender
import org.slf4j.MDC
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import spock.lang.Specification

@Log4j2
class MethodLoggingAspectSpec extends Specification {

    def "logs entry/exit and sets correlation id"() {
        given:
        def ctx = new AnnotationConfigApplicationContext()
        ctx.register(LoggingAopConfig, TestService)
        ctx.refresh()
        def svc = ctx.getBean(TestService)

        def lc = (LoggerContext) LogManager.getContext(false)
        def cfg = lc.configuration
        def app = new ListAppender("mem"); app.start()
        cfg.rootLogger.level = Level.DEBUG
        cfg.rootLogger.addAppender(app, null, null); lc.updateLoggers()

        when:
        svc.work("abc")

        then:
        MDC.get("cid") != null
        app.events.stream().anyMatch { it.message.formattedMessage.contains("→") }
        app.events.stream().anyMatch { it.message.formattedMessage.contains("←") }

        cleanup:
        ctx.close()
    }

    static class TestService {
        static String work(String s) { return s.toUpperCase() }
    }
}
