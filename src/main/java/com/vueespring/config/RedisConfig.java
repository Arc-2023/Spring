package com.vueespring.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@EnableRedisHttpSession(maxInactiveIntervalInSeconds= 3600*24)
//public class RedisConfig {
//    @Bean
//    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory factory){
//        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
//        template.setConnectionFactory(factory);
//
//        // Json序列化配置
////        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
////        ObjectMapper om = new ObjectMapper();
////        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
////        jackson2JsonRedisSerializer.setObjectMapper(om);
////        // String 的序列化
////        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
////
////        // key采用String的序列化方式
////        template.setKeySerializer(stringRedisSerializer);
////        // hash的key也采用String的序列化方式
////        template.setHashKeySerializer(stringRedisSerializer);
////        // value序列化方式采用jackson
////        template.setValueSerializer(jackson2JsonRedisSerializer);
////        // hash的value序列化方式采用jackson
////        template.setHashValueSerializer(jackson2JsonRedisSerializer);
////        template.afterPropertiesSet();
//
//        return template;
//    }
//}
