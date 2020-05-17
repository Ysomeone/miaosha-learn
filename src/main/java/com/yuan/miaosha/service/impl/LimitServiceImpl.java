package com.yuan.miaosha.service.impl;

import com.yuan.miaosha.controller.common.LuaScript;
import com.yuan.miaosha.service.LimitService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author yuan
 * @Date 2020/5/17 1:21
 * @Version 1.0
 */
@Service
public class LimitServiceImpl implements LimitService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Long limit(String key, int limitNum, int expireTime) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class);
        redisScript.setScriptText(LuaScript.limit);
        List<String> keyList = new ArrayList();
        keyList.add(key);
        keyList.add(limitNum + "");
        keyList.add(expireTime + "");
        Long result = stringRedisTemplate.execute(redisScript, keyList);
        return result;
    }
}
