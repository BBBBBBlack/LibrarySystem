package com.bbbbbblack.configuration;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class LogAspect {
    @Pointcut(value = "execution(public * com.bbbbbblack..*Controller.*(..))")
    public void webLog(){}

    @Before("webLog()")
    public void doBefore(JoinPoint jp) throws Throwable{
        ServletRequestAttributes sra=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert sra != null;
        HttpServletRequest request = sra.getRequest();
        System.out.println("aop测试");
        System.out.println("URL:"+request.getRequestURL().toString());
        System.out.println("Method:"+request.getMethod());
        System.out.println(request.getRemoteAddr());
        System.out.println("Args:"+Arrays.toString(jp.getArgs()));
    }
    @AfterReturning(returning = "result",pointcut = "webLog()")
    public void doAfterReturn(Object result){
        System.out.println("------------------方法返回------------------\n"+result);
    }
    @After("webLog()")
    public void doAfter(){
        System.out.println("------------------执行完毕------------------");
    }

}
