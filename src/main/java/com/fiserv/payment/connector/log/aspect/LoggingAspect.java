
package com.fiserv.payment.connector.log.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("""
            execution(* com.fiserv.payment.connector.service.impl.OPITransactionServiceImpl.processRequest(..))
            || execution(* com.fiserv.payment.connector.service.impl.DeviceServiceImpl.processDeviceRequest(..))
            || execution(* com.fiserv.payment.connector.device.impl.*DeviceHandler.connect(..))
            """)
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();

        long startTime = System.currentTimeMillis();

        log.info("START | {}.{}", className, methodName);

        try {
            Object result = joinPoint.proceed();

            long duration = System.currentTimeMillis() - startTime;

            log.info("SUCCESS | {} | durationMs={}", methodName, duration);

            return result;

        } catch (Exception | Error ex) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("FAILED | {}.{} | durationMs={}", className, methodName, duration);
            throw new RuntimeException("Failure in " + className + "." + methodName, ex);
        }
    }
}
