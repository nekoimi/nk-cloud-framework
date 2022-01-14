package com.nekoimi.nk.framework.cache.contract;

import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/21 14:03
 */
public interface CacheService {

    /**
     * 删除缓存
     *
     * @param key
     */
    Mono<Long> delete(String key);

    /**
     * 判断缓存是否存在
     *
     * @param key
     * @return
     */
    Mono<Boolean> exists(String key);

    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    Mono<Object> get(String key);

    /**
     * 获取字符串缓存
     *
     * @param key
     * @return
     */
    Mono<String> getAsString(String key);

    /**
     * 设置缓存
     *
     * @param key
     * @param value
     * @return
     */
    Mono<Boolean> set(String key, Object value);

    /**
     * 对指定缓存加一
     *
     * @param key
     * @return 操作之后的值
     */
    Mono<Long> increment(String key);

    /**
     * @param key
     * @param step
     * @return 操作之后的值
     */
    Mono<Long> increment(String key, long step);

    /**
     * 对指定缓存减一
     *
     * @param key
     * @return
     */
    Mono<Long> decrement(String key);

    /**
     * @param key
     * @param step
     * @return 操作之后的值
     */
    Mono<Long> decrement(String key, long step);
}
