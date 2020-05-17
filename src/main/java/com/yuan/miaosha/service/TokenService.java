package com.yuan.miaosha.service;

/**
 * @Author yuan
 * @Date 2020/5/14 22:00
 * @Version 1.0
 */
public interface TokenService {

    /**
     * 创建一个随机token并存储以string形式存储在redis中
     * @return
     */
    String createToken();

    /**
     * 校验sesssionId是否存储在reids,存储则删除sessionId并返回true，不然返回false
     * @param sessionId
     * @return
     */
    boolean checkToken(String sessionId);
}
