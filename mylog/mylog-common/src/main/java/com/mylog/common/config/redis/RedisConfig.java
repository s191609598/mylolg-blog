package com.mylog.common.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Pss
 * @date 2022/3/21 16:02
 */
@Configuration
public class RedisConfig extends CachingConfigurerSupport {


//    @Bean
//    @SuppressWarnings({"rawtypes", "unchecked"})
//    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<Object, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(redisConnectionFactory);
//        FastJsonRdisSerializer serializer = new FastJsonRdisSerializer(Object.class);
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(serializer);
//        template.setHashKeySerializer(new StringRedisSerializer());
//        template.setHashValueSerializer(serializer);
//        template.afterPropertiesSet();
//        return template;
//    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        // 使用 Jackson2JsonRedisSerializer 序列化值
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        serializer.setObjectMapper(objectMapper);
        // Key 使用 String 序列化
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(RedisSerializer.string());
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }

//    @Bean
//    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
//        // 让 Spring Session 使用 JSON 方式存储
//        return new GenericJackson2JsonRedisSerializer();
//    }
}
