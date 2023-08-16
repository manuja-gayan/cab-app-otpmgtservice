package com.ceyloncab.otpmgtservice.application.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Aspect
@Configuration
@Slf4j
public class ApplicationAopAspect {


    /**
     * adding meta data to mdc cache
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.ceyloncab.otpmgtservice.application.controller.*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] argList = joinPoint.getArgs();

        HttpServletRequest httpServletRequest = getHttpServletRequest(argList);

        String api=AopConstants.NOT_APPLICABLE;
        String userId=AopConstants.NOT_APPLICABLE;
        String channel = AopConstants.NOT_APPLICABLE;
        String uuid = AopConstants.NOT_APPLICABLE;

        if(Objects.nonNull(httpServletRequest)){
            api = httpServletRequest.getRequestURI();
            userId = httpServletRequest.getHeader("UserId");
            channel = httpServletRequest.getHeader("Channel");
            uuid = httpServletRequest.getHeader("UUID");

        }
        MDC.put(AopConstants.MDC_API, api);
        MDC.put(AopConstants.MDC_USERID, userId);
        MDC.put(AopConstants.CHANNEL, channel);
        MDC.put(AopConstants.UUID, uuid);

        return joinPoint.proceed();
    }

    /**
     * get HttpServletRequest from argList
     * @param argList
     * @return
     */
    private HttpServletRequest getHttpServletRequest(Object[] argList) {
        HttpServletRequest httpServletRequest = null;
        try {
            if(argList.length==0){
                log.error("Controller should have at least HttpServletRequest argument");
            } else if (argList.length>1 && argList[1] instanceof HttpServletRequest) {
                httpServletRequest = (HttpServletRequest) argList[1];
            } else if (argList[0] instanceof HttpServletRequest) {
                httpServletRequest = (HttpServletRequest) argList[0];
            }
        } catch (Exception ex) {
            log.error("Error occurred while parsing to HttpServletRequest");
        }

        return httpServletRequest;
    }
}
