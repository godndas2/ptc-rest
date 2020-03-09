package com.example.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.util.StopWatch;

@Aspect
public class LoggingConfig {

    private boolean enabled = true;
    private int callCount = 0;
    private long accumulatedCallTime = 0;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void reset() {
        this.callCount = 0;
        this.accumulatedCallTime = 0;
    }

    public int getCallCount() {
        return callCount;
    }

    public long getCallTime() {
        if (this.callCount > 0)
            return this.accumulatedCallTime / this.callCount;
        else
            return 0;
    }

    @Around("within(@org.springframework.stereotype.Repository *)")
    public Object invoke(ProceedingJoinPoint joinPoint) throws Throwable {
        if (this.enabled) {
            StopWatch stopWatch = new StopWatch(joinPoint.toShortString());

            stopWatch.start("=======================================");
            try {
                return joinPoint.proceed();
            } finally {
                stopWatch.stop();
                synchronized (this) {
                    this.callCount++;
                    this.accumulatedCallTime += stopWatch.getTotalTimeMillis();
                }
            }
        } else {
            return joinPoint.proceed();
        }
    }

}
