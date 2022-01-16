package com.nekoimi.nk.framework.cache.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * nekoimi  2022/1/6 16:03
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.cache")
public class CacheProperties {
    // 使用缓存驱动
    private Driver driver = Driver.rocksdb;
    // rocksdb配置
    private RocksDBOptions rocksdb = new RocksDBOptions();

    @Getter
    @Setter
    public static class RocksDBOptions {
        private String path;
    }

    public enum Driver {
        // redis缓存
        redis,
        // rocksdb缓存
        rocksdb;
    }
}
