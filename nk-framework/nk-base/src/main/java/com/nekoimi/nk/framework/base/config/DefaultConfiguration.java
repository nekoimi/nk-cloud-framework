package com.nekoimi.nk.framework.base.config;

import com.nekoimi.nk.framework.base.config.properties.IdGenProperties;
import com.nekoimi.nk.framework.base.gen.SnowflakeIdGenerator;
import com.nekoimi.nk.framework.core.contract.IdGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

/**
 * nekoimi  2021/12/14 11:19
 */
@Configuration
public class DefaultConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public IdGenerator idGenerator(IdGenProperties properties) {
        return new SnowflakeIdGenerator(properties.getDataCenterId(), properties.getWorkerId());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new SCryptPasswordEncoder();
    }
}
