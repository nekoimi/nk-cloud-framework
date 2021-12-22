package com.nekoimi.nk.framework.redis.service;

import com.nekoimi.nk.framework.cache.contract.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

/**
 * nekoimi  2021/12/21 14:03
 */
@Slf4j
@Service
public class RedisCacheService implements CacheService {
    private final ReactiveRedisTemplate<String, Object> template;

    public RedisCacheService(ReactiveRedisTemplate<String, Object> template) {
        this.template = template;
    }

    @Override
    public Mono<Long> delete(String... keys) {
        return template.delete(keys);
    }

    @Override
    public Mono<Long> delete(Flux<String> keys) {
        return template.delete(keys);
    }

    @Override
    public Mono<Boolean> exists(String key) {
        return template.hasKey(key);
    }

    @Override
    public Mono<Boolean> expireSet(String key, long seconds) {
        return template.expire(key, Duration.ofSeconds(seconds));
    }

    @Override
    public Mono<Boolean> expireAt(String key, LocalDateTime expireAt) {
        return template.expireAt(key, expireAt.toInstant(ZoneOffset.ofHours(8)));
    }

    @Override
    public Mono<Boolean> expireRemove(String key) {
        return template.persist(key);
    }

    @Override
    public Mono<Duration> expireGet(String key) {
        return template.getExpire(key);
    }

    @Override
    public Mono<Object> get(String key) {
        return template.opsForValue().get(key);
    }

    @Override
    public Mono<String> getAsString(String key) {
        return get(key).flatMap(o -> Mono.just(o).cast(String.class));
    }

    @Override
    public Mono<Boolean> set(String key, Object value) {
        return template.opsForValue().set(key, value);
    }

    @Override
    public Mono<Boolean> set(String key, Object value, long seconds) {
        return template.opsForValue().set(key, value, Duration.ofSeconds(seconds));
    }

    @Override
    public Mono<Long> increment(String key) {
        return template.opsForValue().increment(key);
    }

    @Override
    public Mono<Long> increment(String key, long step) {
        return template.opsForValue().increment(key, step);
    }

    @Override
    public Mono<Long> decrement(String key) {
        return template.opsForValue().decrement(key);
    }

    @Override
    public Mono<Long> decrement(String key, long step) {
        return template.opsForValue().decrement(key, step);
    }

    @Override
    public Mono<Long> hDelete(String key, Object... hashKey) {
        return template.opsForHash().remove(key, hashKey);
    }

    @Override
    public Mono<Boolean> hExists(String key, Object hashKey) {
        return template.opsForHash().hasKey(key, hashKey);
    }

    @Override
    public Flux<Object> hKeys(String key) {
        return template.opsForHash().keys(key);
    }

    @Override
    public Flux<Object> hValues(String key) {
        return template.opsForHash().values(key);
    }

    @Override
    public Flux<Map.Entry<Object, Object>> hEntries(String key) {
        return template.opsForHash().entries(key);
    }

    @Override
    public Mono<Object> hGet(String key, Object hashKey) {
        return template.opsForHash().get(key, hashKey);
    }

    @Override
    public Mono<Boolean> hPut(String key, Object hashKey, Object value) {
        return template.opsForHash().put(key, hashKey, value);
    }

    @Override
    public Mono<Boolean> hPutAll(String key, Map<Object, Object> map) {
        return template.opsForHash().putAll(key, map);
    }

    @Override
    public Mono<Long> hIncrement(String key, String hashKey) {
        return template.opsForHash().increment(key, hashKey, 1);
    }

    @Override
    public Mono<Long> hIncrement(String key, String hashKey, long step) {
        return template.opsForHash().increment(key, hashKey, step);
    }

    @Override
    public Mono<Long> hDecrement(String key, String hashKey) {
        return template.opsForHash().increment(key, hashKey, -1);
    }

    @Override
    public Mono<Long> hDecrement(String key, String hashKey, long step) {
        return template.opsForHash().increment(key, hashKey, -step);
    }

    @Override
    public Flux<Object> lGetList(String key) {
        return lGetList(key, 0, -1);
    }

    @Override
    public Flux<Object> lGetList(String key, long start, long end) {
        return template.opsForList().range(key, start, end);
    }

    @Override
    public Mono<Long> lSize(String key) {
        return template.opsForList().size(key);
    }

    @Override
    public Mono<Object> lGetByIndex(String key, long index) {
        return template.opsForList().index(key, index);
    }

    @Override
    public Mono<Long> lAdd(String key, Object value) {
        return template.opsForList().rightPush(key, value);
    }

    @Override
    public Mono<Boolean> lAdd(String key, long index, Object value) {
        return template.opsForList().set(key, index, value);
    }

    @Override
    public Mono<Long> lAddAll(String key, List<Object> values) {
        return template.opsForList().rightPushAll(key, values);
    }

    @Override
    public Mono<Long> lDelete(String key, Object value) {
        return lDelete(key, value, 0);
    }

    @Override
    public Mono<Long> lDelete(String key, Object value, long count) {
        return template.opsForList().remove(key, count, value);
    }

    @Override
    public Mono<Boolean> lTrim(String key, long start, long end) {
        return template.opsForList().trim(key, start, end);
    }

    @Override
    public Flux<Object> sGet(String key) {
        return template.opsForSet().members(key);
    }

    @Override
    public Mono<Long> sSize(String key) {
        return template.opsForSet().size(key);
    }

    @Override
    public Mono<Boolean> sExists(String key, Object value) {
        return template.opsForSet().isMember(key, value);
    }

    @Override
    public Mono<Long> sSet(String key, Object... values) {
        return template.opsForSet().add(key, values);
    }

    @Override
    public Mono<Long> sDelete(String key, Object... values) {
        return template.opsForSet().remove(key, values);
    }
}
