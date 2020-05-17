package com.yuan.miaosha.service.impl;

import com.yuan.miaosha.service.TokenService;
import com.yuan.miaosha.utils.RandomGUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author yuan
 * @Date 2020/5/14 22:00
 * @Version 1.0
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public String createToken() {
        RandomGUIDUtil guidUtil = new RandomGUIDUtil();
        String sessionId = guidUtil.valueAfterMD5;
        redisTemplate.opsForValue().set(sessionId,"1");
        return sessionId;
    }

    @Override
    public boolean checkToken(String sessionId) {
        Boolean flag = redisTemplate.hasKey(sessionId);
        if(flag){
            redisTemplate.delete(sessionId);
            return true;
        }
        return false;
    }
}
