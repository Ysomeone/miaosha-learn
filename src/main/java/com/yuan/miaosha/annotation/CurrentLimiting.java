package com.yuan.miaosha.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author yuan
 * @Date 2020/5/14 22:25
 * @Version 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentLimiting {

    /**
     * 访问的限制次数
     */
     int limit() default 50;

    /**
     * 设置多少秒的限制
     *
     * @return
     */
    int expireTime() default 1;

    /**
     * 统计的key
     *
     */
    String key() default "limitKey";

}
