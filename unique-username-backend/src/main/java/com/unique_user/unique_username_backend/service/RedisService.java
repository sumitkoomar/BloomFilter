package com.unique_user.unique_username_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    private final StringRedisTemplate redisTemplate;


    @Autowired
    public RedisService(StringRedisTemplate redisTemplate) {

        this.redisTemplate = redisTemplate;
    }

    public void cacheUsername(String username){
        redisTemplate.opsForValue().set(username, "true", 10, TimeUnit.HOURS);
    }

    public boolean isUserChached(String username){
        return redisTemplate.hasKey(username);
    }
}
