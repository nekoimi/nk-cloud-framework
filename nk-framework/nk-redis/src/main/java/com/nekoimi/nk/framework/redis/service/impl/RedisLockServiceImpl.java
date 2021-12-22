package com.nekoimi.nk.framework.redis.service.impl;

import com.nekoimi.nk.framework.redis.service.RedisLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.time.Duration;

/**
 * nekoimi  2021/12/21 15:43
 */
@Slf4j
@Service
public class RedisLockServiceImpl implements RedisLockService {
    private static final String LOCK_KEY = "_rlock:";
    private static final String LUA_SCRIPT_LOCK = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
    private static final ByteBuffer LUA_SCRIPT_LOCK_BYTE_BUFFER = ByteBuffer.wrap(LUA_SCRIPT_LOCK.getBytes());
    private final ReactiveRedisTemplate<String, Object> template;

    public RedisLockServiceImpl(ReactiveRedisTemplate<String, Object> template) {
        this.template = template;
    }

    @Override
    public synchronized Mono<Boolean> tryLock(String key, String reqId, long seconds) {
        return template.opsForValue().setIfAbsent(LOCK_KEY + key, reqId, Duration.ofSeconds(seconds));
    }

    @Override
    public synchronized Mono<Boolean> unlock(String key, String reqId) {
        return template.execute(conn -> conn.scriptingCommands()
                .eval(LUA_SCRIPT_LOCK_BYTE_BUFFER, ReturnType.BOOLEAN, 1,
                        createKey(key), ByteBuffer.wrap(reqId.getBytes()))
        ).last().cast(Boolean.class);
    }

    private ByteBuffer createKey(String key) {
        String k = LOCK_KEY + key;
        return ByteBuffer.wrap(k.getBytes());
    }
}
