package com.vueespring.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisOperator {
    @Autowired
    RedisTemplate redisTemplate;
    public boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }
    public long ttl(String key){
        return redisTemplate.getExpire(key);
    }
    public void expire(String key,long time){
        redisTemplate.expire(key,time, TimeUnit.SECONDS);
    }
    public void set(String key,Object value){
        redisTemplate.opsForValue().set(key,value);
    }
    public void del(String k){
        redisTemplate.delete(k);
    }
    public void incr(String k,long v){
        redisTemplate.opsForValue().increment(k,v);
    }

}
