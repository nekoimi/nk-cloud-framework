package com.nekoimi.nk.framework.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * nekoimi  2021/12/19 19:54
 */
@Slf4j
@Configuration
@EnableCaching
public class RedisConfiguration {

    @Bean
    @ConditionalOnBean(value = ObjectMapper.class)
    public ReactiveRedisTemplate<String, Object> reactiveRedisOperations(ReactiveRedisConnectionFactory connectionFactory,
                                                                           ObjectMapper objectMapper) {
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        serializer.setObjectMapper(objectMapper);
        RedisSerializationContext.RedisSerializationContextBuilder<String, Object>
                builder = RedisSerializationContext.newSerializationContext();
        RedisSerializationContext<String, Object> context = builder
                .string(new StringRedisSerializer())
                .key(new StringRedisSerializer())
                .hashKey(new StringRedisSerializer())
                .value(serializer)
                .hashValue(serializer)
                .build();
        return new ReactiveRedisTemplate<>(connectionFactory, context);
    }

    /**
     * 对hash类型的数据操作
     */
    @Bean
    @ConditionalOnBean(value = ReactiveRedisOperations.class)
    public ReactiveHashOperations<String, String, Object> reactiveHashOperations(ReactiveRedisOperations<String, Object> redisOperations) {
        return redisOperations.opsForHash();
    }

    /**
     * 对redis字符串类型数据操作
     */
    @Bean
    @ConditionalOnBean(value = ReactiveRedisOperations.class)
    public ReactiveValueOperations<String, Object> reactiveValueOperations(ReactiveRedisOperations<String, Object> redisOperations) {
        return redisOperations.opsForValue();
    }

    /**
     * 对链表类型的数据操作
     */
    @Bean
    @ConditionalOnBean(value = ReactiveRedisOperations.class)
    public ReactiveListOperations<String, Object> reactiveListOperations(ReactiveRedisOperations<String, Object> redisOperations) {
        return redisOperations.opsForList();
    }

    /**
     * 对无序集合类型的数据操作
     */
    @Bean
    @ConditionalOnBean(value = ReactiveRedisOperations.class)
    public ReactiveSetOperations<String, Object> reactiveSetOperations(ReactiveRedisOperations<String, Object> redisOperations) {
        return redisOperations.opsForSet();
    }

    /**
     * 对有序集合类型的数据操作
     */
    @Bean
    @ConditionalOnBean(value = ReactiveRedisOperations.class)
    public ReactiveZSetOperations<String, Object> reactiveZSetOperations(ReactiveRedisOperations<String, Object> redisOperations) {
        return redisOperations.opsForZSet();
    }
}
