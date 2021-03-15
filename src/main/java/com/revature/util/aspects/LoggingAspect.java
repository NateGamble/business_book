package com.revature.util.aspects;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component // So spring knows about it
public class LoggingAspect {

    private final Logger logger = LogManager.getLogger(LoggingAspect.class);

    @Pointcut("within(com.revature..*) && !within(com.revature.filters..*)")
    public void logAllPointcut() {}

    // All methods will take in a join point
    // Specifies to run before all methods run in quizzard directory and all sub-directories
    @Before("logAllPointcut()")
    public void logMethodStart(JoinPoint jp) {
        String methodSig = extractMethodSignature(jp);
        String argStr = Arrays.toString(jp.getArgs());
        logger.info("{} invoked at {}; input arguments: {}", methodSig, LocalDateTime.now(), argStr);
    }

    private String extractMethodSignature(JoinPoint jp) {
        // Grabs name of class from join point, and name of method signature
        return jp.getTarget().getClass().toString() + "." + jp.getSignature().getName();
    }

    @AfterReturning(pointcut = "logAllPointcut()", returning = "returned")
    public void logMethodReturned(JoinPoint jp, Object returned) {
        String methodSig = extractMethodSignature(jp);
        logger.info("{} was successfully returned at {} with a value of {}", methodSig, LocalDateTime.now(), returned);
    }

    @AfterThrowing(pointcut = "logAllPointcut()", throwing = "e")
    public void logErrorOccurrence(JoinPoint jp, Exception e) {
        String methodSig = extractMethodSignature(jp);
        logger.error("{} was thrown in method {} at {} with message: {}", e.getClass().getSimpleName(), methodSig, LocalDateTime.now(), e.getMessage());
    }
}
