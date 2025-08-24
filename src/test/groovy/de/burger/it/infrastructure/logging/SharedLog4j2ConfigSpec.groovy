package de.burger.it.infrastructure.logging

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.core.config.Configuration
import org.apache.logging.log4j.core.config.Configurator
import org.apache.logging.log4j.core.appender.RollingFileAppender
import org.slf4j.LoggerFactory
import spock.lang.Specification
import spock.lang.Stepwise

@Stepwise
class SharedLog4j2ConfigSpec extends Specification {

    def setupSpec() {
        // Ensure a clean baseline and reload configuration
        System.clearProperty("log.level.root")
        System.clearProperty("app.name")
        System.clearProperty("log.dir")
        Configurator.reconfigure()
    }

    def cleanup() {
        // Reconfigure after each feature to apply potential property changes
        Configurator.reconfigure()
    }

    def "shared log4j2.xml is loaded from classpath and defines Console and File appenders"() {
        given:
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false)
        Configuration cfg = ctx.configuration

        expect:
        cfg != null
        cfg.appenders.containsKey("Console")
        cfg.appenders.containsKey("File")
        cfg.rootLogger.level == Level.INFO  // default from shared config
    }

    def "root logger level can be overridden via -Dlog.level.root"() {
        when:
        System.setProperty("log.level.root", "DEBUG")
        Configurator.reconfigure()
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false)

        then:
        ctx.configuration.rootLogger.level == Level.DEBUG

        cleanup:
        System.clearProperty("log.level.root")
        Configurator.reconfigure()
    }

    def "rolling file appender resolves defaults to logs/ifstatements.log"() {
        given:
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false)
        def app = ctx.configuration.getAppender("File")

        expect:
        app instanceof RollingFileAppender
        def fn = (app as RollingFileAppender).fileName.replace('\\','/')
        fn.endsWith("logs/ifstatements.log")
    }

    def "slf4j facade is bound and emits at INFO without errors"() {
        given:
        def log = LoggerFactory.getLogger(SharedLog4j2ConfigSpec)

        when:
        log.info("probe message from spock (shared log4j2.xml)")

        then:
        log.isInfoEnabled()
    }
}
