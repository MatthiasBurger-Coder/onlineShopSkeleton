package de.burger.it.infrastructure.logging

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.core.config.LoggerConfig
import org.apache.logging.log4j.core.test.appender.ListAppender
import org.aspectj.lang.annotation.Pointcut
import org.junit.jupiter.api.Disabled
import org.slf4j.MDC
import org.springframework.aop.support.AopUtils
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Configuration
import spock.lang.Ignore
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Title
import spock.lang.Unroll

@Title("Unit tests for MethodLoggingAspect (Spock)")
@Narrative("""
Robust PIT-killing spec:
- Target-class logger scoping (no root noise)
- Entry/Exit logs asserted without brittle symbols; level-agnostic
- CID handling: preserves non-blank; generates UUID for blanks (filter + orElseGet)
- Duration via subtraction (regex '(\\d+) ms'); upper bounds kill addition mutants
- START_NS set during proceed(), removed after success/error
- Pointcuts verified (pure Spock)
- Zero-arg args rendering and WARN level gate
""")
@Ignore("Ignore while rework, using MethodLoggingAspectTest.java")
class MethodLoggingAspectSpec extends Specification {

    def setup() {
        MDC.clear()
    }

    // ---------- helpers ----------

    // Access the private ThreadLocal<Long> START_NS
    private static ThreadLocal<Long> startThreadLocal() {
        def f = MethodLoggingAspect.class.getDeclaredField("START_NS")
        f.setAccessible(true)
        (ThreadLocal<Long>) f.get(null)
    }

    // Create isolated logger for the target class (TRACE to capture all)
    private static Map attachIsolatedAppender(Object bean, String appenderName) {
        def lc  = (LoggerContext) LogManager.getContext(false)
        lc.reconfigure()
        def cfg = lc.configuration

        def loggerName = AopUtils.getTargetClass(bean).name

        def app = new ListAppender(appenderName)
        app.start()

        // TRACE to capture DEBUG/INFO regardless of project defaults
        def child = new LoggerConfig(loggerName, Level.TRACE, true)
        child.addAppender(app, Level.TRACE, null)
        cfg.addLogger(loggerName, child)
        lc.updateLoggers()

        [appender: app, loggerName: loggerName, context: lc]
    }

    private static void detachIsolatedAppender(Map handle) {
        def lc  = (LoggerContext) handle.context
        def cfg = lc.configuration
        cfg.removeLogger((String) handle.loggerName)
        handle.appender.stop()
        lc.updateLoggers()
    }

    private static void setLoggerLevel(Map handle, Level level) {
        def lc  = (LoggerContext) handle.context
        def cfg = lc.configuration
        def lcCfg = cfg.getLoggerConfig((String) handle.loggerName)
        lcCfg.setLevel(level)
        lc.updateLoggers()
    }

    private static def lastEvent(ListAppender app) {
        assert !app.events.isEmpty()
        app.events.get(app.events.size() - 1)
    }

    // simple, regex-free extraction of "<n> ms" or "<n>ms"
    private static long extractMs(String msg) {
        if (msg == null) return -1L
        int idxMs = msg.lastIndexOf("ms")
        if (idxMs < 1) return -1L
        int i = idxMs - 1
        while (i >= 0 && Character.isWhitespace(msg.charAt(i))) i--
        int end = i + 1
        while (i >= 0 && Character.isDigit(msg.charAt(i))) i--
        int start = i + 1
        if (start >= end) return -1L
        try { return Long.parseLong(msg.substring(start, end)) } catch (Exception ignored) { return -1L }
    }

    // ---------- tests ----------

    def "GreenPath: entry/exit logs present for target logger; same non-blank cid; duration plausible"() {
        given:
        def ctx = new AnnotationConfigApplicationContext()
        ctx.register(LoggingAopConfig, TestService)
        ctx.refresh()
        def svc = ctx.getBean(TestService)
        def h = attachIsolatedAppender(svc, "mem-green")
        MDC.remove("cid")

        when:
        svc.work("abc")

        then: "we have at least one entry-like and one exit-like message for this method"
        def msgs = h.appender.events*.message*.formattedMessage
        msgs.any { it.contains("TestService") && it.contains("work") && it.contains("(") }
        msgs.any { it.contains("work") && it.contains(")") }

        and: "all events come from the target logger (no root spill)"
        h.appender.events.stream().allMatch { it.loggerName == h.loggerName }

        and: "entry/exit share same non-blank cid"
        def cidValues = h.appender.events.collect { it.contextData?.toMap()?.get("cid")?.toString() }.findAll { it != null }
        assert !cidValues.isEmpty()
        cidValues.every { !it.isBlank() }
        cidValues.unique().size() == 1

        and: "exit has a plausible '<n> ms' duration (via subtraction)"
        def exitMsg = msgs.findAll { it.contains("work") && it.contains(")") && it.contains("OK in") }.last()
        def ms = extractMs(exitMsg)
        assert ms >= 0 && ms < 2_000L

        cleanup:
        detachIsolatedAppender(h); ctx.close(); MDC.clear()
    }

    def "MDC: preserves existing non-blank cid and generates UUID for blanks (filter + orElseGet)"() {
        given:
        def ctx = new AnnotationConfigApplicationContext()
        ctx.register(LoggingAopConfig, TestService)
        ctx.refresh()
        def svc = ctx.getBean(TestService)
        def h = attachIsolatedAppender(svc, "mem-mdc")
        def uuidRe = ~/[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}/

        when: "non-blank cid is kept"
        MDC.put("cid", "fixed-123"); svc.work("x")

        then:
        lastEvent(h.appender).contextData.toMap().get("cid") == "fixed-123"

        when: "blank (spaces) -> generator"
        h.appender.clear(); MDC.put("cid", "   "); svc.work("y")

        then:
        def gen2 = lastEvent(h.appender).contextData.toMap().get("cid")?.toString()
        gen2 != null && uuidRe.matcher(gen2).matches()

        when: "blank (empty) -> generator"
        h.appender.clear(); MDC.put("cid", ""); svc.work("z")

        then:
        def gen3 = lastEvent(h.appender).contextData.toMap().get("cid")?.toString()
        gen3 != null && uuidRe.matcher(gen3).matches()

        cleanup:
        detachIsolatedAppender(h); ctx.close(); MDC.clear()
    }

    @Unroll
    def "CID generation produces UUID for blank input '#desc'"() {
        given:
        def ctx = new AnnotationConfigApplicationContext()
        ctx.register(LoggingAopConfig, DemoService)
        ctx.refresh()
        def svc = ctx.getBean(DemoService)
        def h = attachIsolatedAppender(svc, "mem-mdc-blank")
        def uuidRe = ~/[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}/
        MDC.put("cid", blankVal)

        when:
        svc.concat(null, new Weird())

        then:
        def cid = lastEvent(h.appender).contextData.toMap().get("cid")?.toString()
        cid != null && uuidRe.matcher(cid).matches()

        cleanup:
        detachIsolatedAppender(h); ctx.close(); MDC.clear()

        where:
        desc           | blankVal
        "single space" | " "
        "empty"        | ""
        "tabs"         | "\t\t"
    }

    def "Entry renders args (null & custom) and notifies target logger"() {
        given:
        def ctx = new AnnotationConfigApplicationContext()
        ctx.register(LoggingAopConfig, DemoService)
        ctx.refresh()
        def svc = ctx.getBean(DemoService)
        def h = attachIsolatedAppender(svc, "mem-args")
        MDC.put("cid", " ")

        when:
        svc.concat(null, new Weird())

        then:
        def entry = h.appender.events*.message*.formattedMessage.find { it.contains("DemoService") && it.contains("concat") }
        entry != null
        entry.contains("null") && entry.contains("Weird{}")
        h.appender.events.stream().allMatch { it.loggerName == h.loggerName }

        cleanup:
        detachIsolatedAppender(h); ctx.close(); MDC.clear()
    }

    def "Zero-arg method renders empty '[]' and has positive duration"() {
        given:
        def ctx = new AnnotationConfigApplicationContext()
        ctx.register(LoggingAopConfig, ComplexArgsService)
        ctx.refresh()
        def svc = ctx.getBean(ComplexArgsService)
        def h = attachIsolatedAppender(svc, "mem-zero")

        when:
        svc.zero()

        then:
        def entry = h.appender.events*.message*.formattedMessage.find { it.contains("ComplexArgsService") && it.contains("zero(") }
        entry != null && entry.contains("[]")
        def exit = h.appender.events*.message*.formattedMessage.findAll { it.contains("zero()") && it.contains("OK in") }.last()
        def ms0 = extractMs(exit)
        assert ms0 >= 0

        cleanup:
        detachIsolatedAppender(h); ctx.close()
    }

    def "elapsed time computed via subtraction and START_NS removed on success"() {
        given:
        def ctx = new AnnotationConfigApplicationContext()
        ctx.register(LoggingAopConfig, DemoService)
        ctx.refresh()
        def svc = ctx.getBean(DemoService)
        def h = attachIsolatedAppender(svc, "mem-elapsed-ok")
        startThreadLocal().remove(); assert startThreadLocal().get() == null

        when:
        svc.sleepy()

        then:
        def exit = h.appender.events*.message*.formattedMessage.findAll { it.contains("sleepy()") && it.contains("OK in") }.last()
        def ms1 = extractMs(exit)
        assert ms1 >= 0 && ms1 < 60_000L
        startThreadLocal().get() == null

        cleanup:
        detachIsolatedAppender(h); ctx.close()
    }

    def "elapsed time computed via subtraction and START_NS removed on exception"() {
        given:
        def ctx = new AnnotationConfigApplicationContext()
        ctx.register(LoggingAopConfig, FailingService)
        ctx.refresh()
        def svc = ctx.getBean(FailingService)
        def h = attachIsolatedAppender(svc, "mem-elapsed-err")
        startThreadLocal().remove(); assert startThreadLocal().get() == null

        when:
        svc.explode("boom")

        then:
        thrown(IllegalStateException)
        def err = h.appender.events*.message*.formattedMessage.findAll { it.contains("explode") && (it.contains("failed in") || it.toLowerCase().contains("failed")) }.last()
        def ms2 = extractMs(err)
        assert ms2 >= 0 && ms2 < 60_000L
        startThreadLocal().get() == null

        cleanup:
        detachIsolatedAppender(h); ctx.close(); MDC.clear()
    }

    def "START_NS is non-null during proceed() and removed after return"() {
        given:
        def ctx = new AnnotationConfigApplicationContext()
        ctx.register(LoggingAopConfig, TLProbeService)
        ctx.refresh()
        def svc = ctx.getBean(TLProbeService)
        def h = attachIsolatedAppender(svc, "mem-probe")
        startThreadLocal().remove()

        when:
        def seen = svc.ping()

        then:
        seen != null && seen != "null"
        startThreadLocal().get() == null
        def exit = h.appender.events*.message*.formattedMessage.findAll { it.contains("ping()") && it.contains("OK in") }.last()
        def ms3 = extractMs(exit)
        assert ms3 >= 0 && ms3 < 60_000L

        cleanup:
        detachIsolatedAppender(h); ctx.close()
    }

    def "appOps matches application beans and proxies and logs"() {
        given:
        def ctx = new AnnotationConfigApplicationContext()
        ctx.register(LoggingAopConfig, TestService)
        ctx.refresh()
        def svc = ctx.getBean(TestService)
        def h = attachIsolatedAppender(svc, "mem-appops")

        expect:
        AopUtils.isAopProxy(svc)

        when:
        svc.work("z")

        then:
        h.appender.events*.message*.formattedMessage.any { it.contains("TestService") && it.contains("work") }
        h.appender.events.stream().allMatch { it.loggerName == h.loggerName }

        cleanup:
        detachIsolatedAppender(h); ctx.close()
    }

    def "configClasses pointcut excludes @Configuration methods"() {
        given:
        def ctx = new AnnotationConfigApplicationContext()
        ctx.register(LoggingAopConfig, ConfigClassBean)
        ctx.refresh()
        def cfgBean = ctx.getBean(ConfigClassBean)
        def h = attachIsolatedAppender(cfgBean, "mem-config-excl")

        when:
        cfgBean.configMethod("x")

        then:
        h.appender.events.isEmpty()

        cleanup:
        detachIsolatedAppender(h); ctx.close()
    }

    def "Level gate: when logger is WARN, no entry/exit are emitted"() {
        given:
        def ctx = new AnnotationConfigApplicationContext()
        ctx.register(LoggingAopConfig, TestService)
        ctx.refresh()
        def svc = ctx.getBean(TestService)
        def h = attachIsolatedAppender(svc, "mem-warn")
        setLoggerLevel(h, Level.WARN)

        when:
        svc.work("mute")

        then:
        h.appender.events.isEmpty()

        cleanup:
        detachIsolatedAppender(h); ctx.close()
    }

    def "pointcut methods are callable and verified (Spock only)"() {
        given: "Spock Spy to observe direct calls"
        def aspect = Spy(MethodLoggingAspect)

        when:
        aspect.appOps()
        aspect.configClasses()

        then:
        1 * aspect.appOps()
        1 * aspect.configClasses()

        and: "annotations as expected"
        def appOpsPc = MethodLoggingAspect.class.getMethod("appOps").getAnnotation(Pointcut)
        assert appOpsPc != null
        def expected = [
                "de.burger.it..application..*(..)",
                "de.burger.it..domain..*(..)",
                "de.burger.it..adapters..*(..)",
                "de.burger.it..infrastructure..*(..)"
        ]
        assert expected.every { frag -> appOpsPc.value().contains(frag) }
        def cfgPc = MethodLoggingAspect.class.getMethod("configClasses").getAnnotation(Pointcut)
        assert cfgPc != null
        assert cfgPc.value() == "@within(org.springframework.context.annotation.Configuration)"
    }

    // ---------- fixtures ----------

    static class TestService {
        String work(String s) {
            // guarantee >0ms
            Thread.sleep(10)
            return s?.toUpperCase()
        }
    }

    static class DemoService {
        String concat(String a, Object b) { String.valueOf(a) + String.valueOf(b) }
        void sleepy() { Thread.sleep(5) }
    }

    static class FailingService {
        String explode(String input) { throw new IllegalStateException("kaboom: " + input) }
    }

    static class ComplexArgsService {
        void zero() { /* intercepted, no args */ }
    }

    // Reads START_NS during proceed()
    static class TLProbeService {
        String ping() {
            def f = MethodLoggingAspect.class.getDeclaredField("START_NS")
            f.setAccessible(true)
            def tl = (ThreadLocal<Long>) f.get(null)
            def v = tl.get()
            Thread.sleep(5)
            String.valueOf(v)
        }
    }

    static class Weird {
        @Override String toString() { "Weird{}" }
    }

    @Configuration
    static class ConfigClassBean {
        String configMethod(String input) { "CFG-" + input }
    }
}
