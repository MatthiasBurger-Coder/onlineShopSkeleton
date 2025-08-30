package de.burger.it.infrastructure.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.Arrays;

@Aspect
public class MethodLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(MethodLoggingAspect.class);

    @Pointcut(
            "execution(public * de.burger.it..application..*(..)) || " +
                    "execution(public * de.burger.it..domain..*(..)) || " +
                    "execution(public * de.burger.it..adapters..*(..)) || " +
                    "execution(public * de.burger.it..infrastructure..*(..))"
    )
    public void appOps() {}

    @Pointcut("@within(org.springframework.context.annotation.Configuration)")
    public void configClasses() {}

    @Around("appOps() && !configClasses() && !@annotation(de.burger.it.infrastructure.logging.SuppressLogging)")
    public Object around(final ProceedingJoinPoint pjp) throws Throwable {
        // ensure correlation id (no if; use Optional)
        MDC.put("cid", java.util.Optional.ofNullable(MDC.get("cid"))
                .filter(s -> !s.isBlank())
                .orElseGet(() -> UUID.randomUUID().toString()));

        final String method = pjp.getSignature().toShortString();
        final String args = Arrays.stream(pjp.getArgs())
                .map(this::safeToString)
                .collect(Collectors.joining(", ", "[", "]"));

        final long t0 = System.nanoTime();
        log.debug("→ {} {}", method, args);

        try {
            Object result = pjp.proceed();
            long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - t0);
            log.info("← {} OK in {} ms", method, elapsedMs);
            return result;
        } catch (Throwable ex) {
            long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - t0);
            log.error("✖ {} failed in {} ms: {}", method, elapsedMs, ex.getMessage(), ex);
            throw ex;
        }
    }

    private String safeToString(Object o) {
        return java.util.Optional.ofNullable(o).map(Objects::toString).orElse("null");
    }
}
