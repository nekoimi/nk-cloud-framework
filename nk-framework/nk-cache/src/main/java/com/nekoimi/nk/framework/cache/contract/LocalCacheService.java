package com.nekoimi.nk.framework.cache.contract;

import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/21 14:03
 */
public interface LocalCacheService {

    /**
     * 删除缓存
     *
     * @param key
     */
    Mono<Void> delete(String key);

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
    Mono<byte[]> get(String key);

    /**
     * 设置缓存
     *
     * @param key
     * @param value
     * @return
     */
    Mono<Boolean> set(String key, byte[] value);
}
