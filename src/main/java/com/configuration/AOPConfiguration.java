package com.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class AOPConfiguration {

    private final static Log LOG = LogFactory.getLog(AOPConfiguration.class);

    @Before("execution(* com.service.*.get*(..))")
    public void before(JoinPoint joinPoint) {
        LOG.info("Going to call " + joinPoint.getSignature().getName() + "with parameter" + Arrays.toString(joinPoint.getArgs()));
    }

    @After("execution(* com.service.*.get*(..))")
    public void after(JoinPoint joinPoint) {
        LOG.info("After calling " + joinPoint.getSignature().getName());
    }
}
