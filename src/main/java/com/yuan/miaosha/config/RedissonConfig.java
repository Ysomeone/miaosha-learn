package com.yuan.miaosha.config;

import com.yuan.miaosha.utils.RedissLockUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.redisson.config.Config;

/**
 * @Author yuan
 * @Date 2020/4/12 19:14
 * @Version 1.0
 */
@Configuration
public class RedissonConfig {

    @Value("${redisson.address}")
    private String addressUrl;

    @Bean
    public RedissonClient getRedisson() throws Exception{
        RedissonClient redisson = null;
        Config config = new Config();
        config.useSingleServer()
                .setAddress(addressUrl);
        redisson = Redisson.create(config);

        return redisson;
    }

    /**
     * 装配locker类，并将实例注入到RedissLockUtil中
     * @return
     */
    @Bean
    RedissLockUtil redissLockUtil(RedissonClient redissonClient) {
        RedissLockUtil redissLockUtil = new RedissLockUtil();
        redissLockUtil.setRedissonClient(redissonClient);
        return redissLockUtil;
    }
}
