package de.burger.it.infrastructure.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.test.appender.ListAppender;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.slf4j.MDC;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;


import static org.junit.jupiter.api.Assertions.*;
@SpringJUnitConfig(MethodLoggingAspectTest.Config.class)
class MethodLoggingAspectTest {
    @Configuration
    @Import(LoggingAopConfig.class)
    static class Config {
        @Bean TestService testService() { return new TestService(); }
        @Bean DemoService demoService() { return new DemoService(); }
        @Bean FailingService failingService() { return new FailingService(); }
        @Bean FailingSleeperService failingSleeperService() { return new FailingSleeperService(); }
        @Bean ConfigClassBean configClassBean() { return new ConfigClassBean(); }
        @Bean ClearThenReturnService clearThenReturnService() { return new ClearThenReturnService(); }
        @Bean ClearThenThrowService  clearThenThrowService()  { return new ClearThenThrowService(); }
    }

    @BeforeEach
    void clearMdc() {
        MDC.clear();
    }

    @Test
    void greenPath_entryExit_and_mdcGenerated(@Autowired TestService svc) {
        var app = attachAppenderFor(svc, "mem-green-java");
        MDC.remove("cid");

        svc.work("abc");

        assertTrue(app.getEvents().stream().anyMatch(e -> e.getLevel() == Level.INFO && e.getMessage().getFormattedMessage().contains("→")));
        assertTrue(app.getEvents().stream().anyMatch(e -> e.getLevel() == Level.INFO && e.getMessage().getFormattedMessage().contains("←")));

        assertTrue(app.getEvents().stream().anyMatch(e -> e.getLevel() == Level.INFO && e.getMessage().getFormattedMessage().contains("TestService.work(..)")));
        assertTrue(app.getEvents().stream().anyMatch(e -> e.getLevel() == Level.INFO && e.getMessage().getFormattedMessage().contains("[abc]")));

        var exitMsg = app.getEvents().stream()
                .filter(e -> e.getLevel() == Level.INFO && e.getMessage().getFormattedMessage().contains("←"))
                .findFirst().get().getMessage().getFormattedMessage();
        var ms = Long.parseLong(exitMsg.replaceAll(".*OK in (\\d+) ms.*", "$1"));
        assertTrue(ms >= 0 && ms < 2000L);

        var exitEvt = app.getEvents().stream()
                .filter(e -> e.getLevel() == Level.INFO && e.getMessage().getFormattedMessage().contains("←"))
                .findFirst().get();
        assertTrue(exitEvt.getContextData().toMap().containsKey("cid"));
        assertFalse(exitEvt.getContextData().toMap().get("cid").trim().isEmpty());

        MDC.remove("cid");
    }

    @Test
    void mdc_preservesExistingAndReplacesBlank(@Autowired TestService svc) {
        var app = attachAppenderFor(svc, "mem-mdc-java");

        MDC.put("cid", "fixed-123");
        svc.work("x");
        assertFalse(app.getEvents().isEmpty());
        var e1 = lastEvent(app);
        assertEquals("fixed-123", e1.getContextData().toMap().get("cid"));

        app.clear();
        MDC.put("cid", "   ");
        svc.work("y");
        assertFalse(app.getEvents().isEmpty());
        var e2 = lastEvent(app);
        assertFalse(e2.getContextData().toMap().get("cid").trim().isBlank());
        assertNotEquals("   ", e2.getContextData().toMap().get("cid"));

        MDC.remove("cid");
    }

    @Test
    void entry_rendersNullAndCustomObject_and_generatesCidWhenBlank(@Autowired DemoService svc) {
        var app = attachAppenderFor(svc, "mem-args-java");

        MDC.put("cid", " ");
        svc.concat(null, new Weird());

        var cid = MDC.get("cid");
        assertNotNull(cid);
        assertFalse(cid.isBlank());

        var entry = app.getEvents().stream()
                .map(e -> e.getMessage().getFormattedMessage())
                .filter(m -> m.contains("→") && m.contains("DemoService.concat("))
                .reduce((a, b) -> b).orElse("");
        assertTrue(entry.contains("["));
        assertTrue(entry.contains("]"));
        assertTrue(entry.contains("null"));
        assertTrue(entry.contains("Weird{}"));
    }

    @Test
    void exit_cleansThreadLocal_and_durationIsPositive(@org.springframework.beans.factory.annotation.Autowired DemoService svc) throws Exception {
        var app = attachAppenderFor(svc, "mem-exit-java");

        svc.sleepy();

        var msg = app.getEvents().stream()
                .map(e -> e.getMessage().getFormattedMessage())
                .filter(m -> m.contains("←") && m.contains("sleepy()"))
                .findFirst().orElse(null);
        assertNotNull(msg);
        var elapsed = Long.parseLong(msg.replaceAll(".*OK in (\\d+) ms.*", "$1"));
        assertTrue(elapsed >= 3 && elapsed < 60000);

        var tlField = MethodLoggingAspect.class.getDeclaredField("START_NS");
        tlField.setAccessible(true);
        @SuppressWarnings("unchecked")
        var threadLocal = (ThreadLocal<Long>) tlField.get(null);
        assertEquals(0L, threadLocal.get());
    }

    @Test
    void afterThrowing_logsError_withThrowable_and_cleansThreadLocal(@org.springframework.beans.factory.annotation.Autowired FailingService svc) throws Exception {
        var app = attachAppenderFor(svc, "mem-throw-java");
        MDC.remove("cid");

        assertThrows(IllegalStateException.class, () -> svc.explode("boom"));

        var errEvt = app.getEvents().stream().filter(e -> e.getLevel() == Level.ERROR).findFirst().orElse(null);
        assertNotNull(errEvt);
        assertNotNull(errEvt.getThrown());
        assertTrue(errEvt.getMessage().getFormattedMessage().contains("FailingService.explode(..)"));
        assertTrue(errEvt.getMessage().getFormattedMessage().contains("failed"));
        assertTrue(errEvt.getContextData().toMap().containsKey("cid"));

        var em = errEvt.getMessage().getFormattedMessage();
        var d = Long.parseLong(em.replaceAll(".*failed in (\\d+) ms.*", "$1"));
        assertTrue(d >= 0 && d < 60000);

        var tlField = MethodLoggingAspect.class.getDeclaredField("START_NS");
        tlField.setAccessible(true);
        @SuppressWarnings("unchecked")
        var threadLocal = (ThreadLocal<Long>) tlField.get(null);
        assertEquals(0L, threadLocal.get());

        MDC.remove("cid");
    }

    @Test
    void afterThrowing_durationReflectsOnEnter_andThreadLocalCleaned(@Autowired FailingSleeperService svc) throws Exception {
        var app = attachAppenderFor(svc, "mem-throw-sleep-java");
        MDC.remove("cid");

        assertThrows(IllegalStateException.class, () -> svc.explodeAfterSleep("X"));

        var errEvt = app.getEvents().stream()
                .filter(e -> e.getLevel() == Level.ERROR)
                .findFirst().orElse(null);
        assertNotNull(errEvt);
        assertTrue(errEvt.getMessage().getFormattedMessage().contains("failed"));

        var em = errEvt.getMessage().getFormattedMessage();
        var d = Long.parseLong(em.replaceAll(".*failed in (\\d+) ms.*", "$1"));
        assertTrue(d >= 5 && d < 60000, "elapsed should reflect the pre-throw sleep");

        var tlField = MethodLoggingAspect.class.getDeclaredField("START_NS");
        tlField.setAccessible(true);
        @SuppressWarnings("unchecked")
        var threadLocal = (ThreadLocal<Long>) tlField.get(null);
        assertEquals(0L, threadLocal.get());

        MDC.remove("cid");
    }

    @Test
    void pointcut_methods_are_callable_and_verified() throws Exception {
        var aspect = Mockito.spy(new MethodLoggingAspect());

        aspect.appOps();
        aspect.configClasses();

        Mockito.verify(aspect).appOps();
        Mockito.verify(aspect).configClasses();

        var appOpsPc = MethodLoggingAspect.class.getMethod("appOps").getAnnotation(Pointcut.class);
        assertNotNull(appOpsPc);
        assertTrue(appOpsPc.value().contains("de.burger.it..application..*(..)"));
        assertTrue(appOpsPc.value().contains("de.burger.it..domain..*(..)"));
        assertTrue(appOpsPc.value().contains("de.burger.it..adapters..*(..)"));
        assertTrue(appOpsPc.value().contains("de.burger.it..infrastructure..*(..)"));

        var cfgPc = MethodLoggingAspect.class.getMethod("configClasses").getAnnotation(Pointcut.class);
        assertNotNull(cfgPc);
        assertEquals("@within(org.springframework.context.annotation.Configuration)", cfgPc.value());
    }

    // -------- helpers --------
    private static ListAppender attachAppenderFor(Object bean, String name) {
        var lc = (LoggerContext) LogManager.getContext(false);
        lc.reconfigure();
        var cfg = lc.getConfiguration();

        var loggerName = AopUtils.getTargetClass(bean).getName();

        var app = new ListAppender(name);
        app.start();

        LoggerConfig effective = cfg.getLoggerConfig(loggerName);
        effective.addAppender(app, Level.INFO, null);

        cfg.getRootLogger().setLevel(Level.INFO);
        lc.updateLoggers();
        return app;
    }

    private static org.apache.logging.log4j.core.LogEvent lastEvent(ListAppender app) {
        assertFalse(app.getEvents().isEmpty());
        return app.getEvents().getLast();
    }

    @Test
    void appOps_matches_applicationBeans_and_proxiesAndLogs(@org.springframework.beans.factory.annotation.Autowired TestService svc) {
        // Should be proxied because appOps() matches and AOP is enabled
        Assertions.assertTrue(AopUtils.isAopProxy(svc));
        var app = attachAppenderFor(svc, "mem-appops-java");
        svc.work("z");
        Assertions.assertTrue(app.getEvents().stream().anyMatch(e -> e.getMessage().getFormattedMessage().contains("TestService.work(..)")));
    }

    @Test
    void configClasses_pointcut_excludes_Configuration_methods(@org.springframework.beans.factory.annotation.Autowired ConfigClassBean cfgBean) {
        // Even though the method is public and under our package, @within(Configuration) must exclude it
        var app = attachAppenderFor(cfgBean, "mem-config-excl-java");
        cfgBean.configMethod("x");
        // No entry/exit logs should be produced for config classes
        Assertions.assertTrue(app.getEvents().isEmpty());
    }

    // ---- compact all: one test checks enter/return/throw ----
    @Test
    void compact_all_advices_in_one(@Autowired DemoService demo, @Autowired FailingService failing) throws Exception {
        // success path (onEnter + onReturn)
        var app1 = attachAppenderFor(demo, "mem-compact-1");
        MDC.remove("cid");
        demo.sleepy();
        var events1 = app1.getEvents();
        assertTrue(events1.stream().anyMatch(e -> e.getMessage().getFormattedMessage().contains("→")));
        assertTrue(events1.stream().anyMatch(e -> e.getMessage().getFormattedMessage().contains("←")));
        var okMsg = events1.stream().map(e -> e.getMessage().getFormattedMessage()).filter(m -> m.contains("←")).findFirst().orElse("");
        assertFalse(okMsg.isEmpty());
        var okMs = Long.parseLong(okMsg.replaceAll(".*OK in (\\d+) ms.*", "$1"));
        assertTrue(okMs >= 3 && okMs < 60000);
        var f = MethodLoggingAspect.class.getDeclaredField("START_NS");
        f.setAccessible(true);
        @SuppressWarnings("unchecked") var tl = (ThreadLocal<Long>) f.get(null);
        assertEquals(0L, tl.get());

        // failure path (onEnter + onThrow)
        var app2 = attachAppenderFor(failing, "mem-compact-2");
        MDC.remove("cid");
        assertThrows(IllegalStateException.class, () -> failing.explode("boom"));
        var events2 = app2.getEvents();
        assertTrue(events2.stream().anyMatch(e -> e.getMessage().getFormattedMessage().contains("→")));
        var errEvt = events2.stream().filter(e -> e.getLevel() == Level.ERROR).findFirst().orElse(null);
        assertNotNull(errEvt);
        var em = errEvt.getMessage().getFormattedMessage();
        assertTrue(em.contains("failed"));
        var errMs = Long.parseLong(em.replaceAll(".*failed in (\\d+) ms.*", "$1"));
        assertTrue(errMs >= 0 && errMs < 60000);
        assertEquals(0L, tl.get());

        MDC.remove("cid");
    }
    @Test
    void onReturn_fallback_branch_startedNull(@Autowired ClearThenReturnService svc) {
        // Attach to the concrete target logger
        var app = attachAppenderFor(svc, "mem-fallback-ret-java");

        // Call clears START_NS mid-call -> @AfterReturning uses (end - end)
        svc.run();

        // Expect an INFO exit with ~0 ms (difference of two near-consecutive nanoTime() calls)
        var exit = app.getEvents().stream()
                .filter(e -> e.getLevel() == Level.INFO && e.getMessage().getFormattedMessage().contains("←"))
                .findFirst().orElseThrow();
        var ms = Long.parseLong(exit.getMessage().getFormattedMessage().replaceAll(".*\\b(\\d+) ms\\b.*", "$1"));

        // Be tolerant but tiny; should be 0 in practice, <= 1 ms in worst case
        assertTrue(ms <= 1L, "fallback elapsed should be ≈ 0 ms");
    }

    @Test
    void onThrow_fallback_branch_startedNull(@Autowired ClearThenThrowService svc) {
        var app = attachAppenderFor(svc, "mem-fallback-throw-java");

        // Triggers clear + throws -> @AfterThrowing uses fallback end - end
        assertThrows(IllegalArgumentException.class, svc::run);

        var err = app.getEvents().stream()
                .filter(e -> e.getLevel() == Level.ERROR && e.getMessage().getFormattedMessage().contains("failed"))
                .findFirst().orElseThrow();
        var ms = Long.parseLong(err.getMessage().getFormattedMessage().replaceAll(".*\\b(\\d+) ms\\b.*", "$1"));

        assertTrue(ms <= 1L, "fallback elapsed should be ≈ 0 ms");
    }


    // ---- fixtures to force started == null in the advices ----
    static class ClearThenReturnService {
        public void run() {
            // Clear START_NS mid-call so @AfterReturning sees started == null
            try {
                var f = MethodLoggingAspect.class.getDeclaredField("START_NS");
                f.setAccessible(true);
                @SuppressWarnings("unchecked")
                var tl = (ThreadLocal<Long>) f.get(null);
                tl.remove();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class ClearThenThrowService {
        public void run() {
            // Clear START_NS mid-call so @AfterThrowing sees started == null
            try {
                var f = MethodLoggingAspect.class.getDeclaredField("START_NS");
                f.setAccessible(true);
                @SuppressWarnings("unchecked")
                var tl = (ThreadLocal<Long>) f.get(null);
                tl.remove();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            throw new IllegalArgumentException("forced");
        }
    }

    static class TestService {
        public void work(String s) {
            try { Thread.sleep(10); } catch (InterruptedException ignored) {}
        }
    }
    static class DemoService {
        public void concat(String a, Object b) {
        }
        public void sleepy() { try { Thread.sleep(5); } catch (InterruptedException ignored) {} }
    }
    static class FailingService {
        public void explode(String input) { throw new IllegalStateException("kaboom: " + input); }
    }
    static class FailingSleeperService {
        public void explodeAfterSleep(String input) {
            try { Thread.sleep(7); } catch (InterruptedException ignored) {}
            throw new IllegalStateException("boom: " + input);
        }
    }
    static class Weird { @Override public String toString() { return "Weird{}"; } }

    // A @Configuration class to ensure configClasses() pointcut excludes it
    @Configuration
    static class ConfigClassBean {
        public void configMethod(String in) {
        }
    }
}
