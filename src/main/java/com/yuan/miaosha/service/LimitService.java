package com.yuan.miaosha.service;

/**
 * @Author yuan
 * @Date 2020/5/17 1:20
 * @Version 1.0
 */
public interface LimitService {

    /**
     * 限流
     *
     * @param key
     * @param limitNum   限制通过的大小
     * @param expireTime 过期事件，单位秒 (limitNum ,expireTime两个加起来就是几秒只可以几个访问通次数)
     * @return
     */
    Long limit(String key, int limitNum, int expireTime);

}
