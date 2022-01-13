package com.nekoimi.nk.framework.rocksdb.service.impl;

import com.nekoimi.nk.framework.cache.contract.CacheService;
import org.rocksdb.RocksDB;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2022/1/6 15:57
 */
public class RocksDBService implements CacheService {

    static {
        RocksDB.loadLibrary();
    }

    @Override
    public Mono<Long> delete(String key) {
        return null;
    }

    @Override
    public Mono<Boolean> exists(String key) {
        return null;
    }

    @Override
    public Mono<Object> get(String key) {
        return null;
    }

    @Override
    public Mono<String> getAsString(String key) {
        return null;
    }

    @Override
    public Mono<Boolean> set(String key, Object value) {
        return null;
    }

    @Override
    public Mono<Long> increment(String key) {
        return null;
    }

    @Override
    public Mono<Long> increment(String key, long step) {
        return null;
    }

    @Override
    public Mono<Long> decrement(String key) {
        return null;
    }

    @Override
    public Mono<Long> decrement(String key, long step) {
        return null;
    }
}
