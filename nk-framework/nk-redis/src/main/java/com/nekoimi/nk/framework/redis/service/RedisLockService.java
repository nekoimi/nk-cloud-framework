package com.nekoimi.nk.framework.redis.service;

import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/21 15:40
 *
 * redis 分布式锁
 */
public interface RedisLockService {

    /**
     * 尝试获取锁
     * @param key
     * @param reqId
     * @param seconds 锁有效时间(秒)
     * @return
     */
    Mono<Boolean> tryLock(String key, String reqId, long seconds);

    /**
     * 释放锁
     * @param key
     * @param reqId
     * @return
     */
    Mono<Boolean> unlock(String key, String reqId);
}
