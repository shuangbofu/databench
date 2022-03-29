package org.example.databench.web.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.databench.web.config.DaoContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class DaoContextAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(DaoContextAspect.class);
    @Autowired
    private DaoContext daoContext;

    @Around("execution(* org.example.databench.web.controller.*.*(..)) || " +
            "execution(* org.example.databench.web.controller.*.*.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
//                .getRequest();
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        daoContext.removeAll();

//        if(method.)
        return pjp.proceed();
    }
}
