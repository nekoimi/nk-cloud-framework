package com.nekoimi.nk.framework.rocksdb.config;

import com.nekoimi.nk.framework.cache.contract.CacheService;
import com.nekoimi.nk.framework.rocksdb.service.impl.RocksDBService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * nekoimi  2022/1/6 15:56
 */
@Configuration
public class RocksDBConfiguration {

    @Bean
    public CacheService rocksdbService() {
        return new RocksDBService();
    }
}
