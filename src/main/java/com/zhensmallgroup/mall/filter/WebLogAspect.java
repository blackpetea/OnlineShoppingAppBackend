package com.zhensmallgroup.mall.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

//打印请求和响应信息
@Aspect
@Component
public class WebLogAspect {
    private final Logger log = LoggerFactory.getLogger(WebLogAspect.class);
    @Pointcut("execution(public * com.zhensmallgroup.mall.controller.*.*(..))")
    public void webLog(){

    }

    //before process the request
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint){
        //receive request and log request content

        //get the request
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        log.info("URL: " + request.getRequestURL().toString());
        log.info("HTTP_METHOD: " + request.getMethod());
        log.info("IP: " + request.getRemoteAddr());
        //use joinPoint to get which of our class and method is processing the current request
        log.info("CLASS_METHOD: " +  joinPoint.getSignature().getDeclaringTypeName() + "." +
                joinPoint.getSignature().getName());
        log.info("ARGS: " + Arrays.toString(joinPoint.getArgs()));
    }

    // after process the request
    @AfterReturning(returning = "res", pointcut = "webLog()")
    public void doAfterReturning(Object res) throws JsonProcessingException {
        //return content after handled the request

        //res might be an object
        //use ObjectMapper to convert object to json
        log.info("RESPONSE: " + new ObjectMapper().writeValueAsString(res));

    }


}
