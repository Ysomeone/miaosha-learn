package com.yuan.miaosha.interceptor;

import com.yuan.miaosha.annotation.CurrentLimiting;
import com.yuan.miaosha.controller.common.ApiConstants;
import com.yuan.miaosha.controller.exception.ApiException;
import com.yuan.miaosha.service.LimitService;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * @Author yuan
 * @Date 2020/5/14 22:27
 * @Version 1.0
 */
@Log4j2
@Aspect
@Configuration
public class CurrentLimitingInterceptor {


    @Resource
    private LimitService limitService;

    @Around("execution(public * *(..)) && @annotation(com.yuan.miaosha.annotation.CurrentLimiting)")
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        if (method.isAnnotationPresent(CurrentLimiting.class)) {
            CurrentLimiting currentLimiting = method.getAnnotation(CurrentLimiting.class);
            Long limit = limitService.limit(currentLimiting.key(), currentLimiting.limit(), currentLimiting.expireTime());
            if (limit==-1) {
                throw new ApiException("人数过多，请再次尝试！", ApiConstants.ERROR700);
            }
        }
        return pjp.proceed();
    }


}
