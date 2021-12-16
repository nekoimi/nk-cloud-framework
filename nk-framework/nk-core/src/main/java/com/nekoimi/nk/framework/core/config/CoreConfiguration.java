package com.nekoimi.nk.framework.core.config;

import com.nekoimi.nk.framework.core.config.properties.IdGenProperties;
import com.nekoimi.nk.framework.core.contract.IdGenerator;
import com.nekoimi.nk.framework.core.generator.SnowflakeIdGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * nekoimi  2021/12/14 11:19
 */
@Configuration
public class CoreConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public IdGenerator idGenerator(IdGenProperties properties) {
        return new SnowflakeIdGenerator(properties.getDataCenterId(), properties.getWorkerId());
    }
}
