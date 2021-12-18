package com.nekoimi.nk.framework.mybatis.config;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.nekoimi.nk.framework.core.contract.IdGenerator;
import com.nekoimi.nk.framework.mybatis.injector.NkExtensionSqlInjector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * nekoimi  2021/12/13 17:00
 */
@Slf4j
@Configuration
@EnableTransactionManagement
public class MyBatisPlusConfiguration {

    @Bean
    public ISqlInjector nkExtensionSqlInjector() {
        return new NkExtensionSqlInjector();
    }

    @Bean
    public IdentifierGenerator identifierGenerator(IdGenerator idGenerator) {
        return new IdentifierGenerator() {
            @Override
            public Number nextId(Object entity) {
                return idGenerator.id();
            }

            @Override
            public String nextUUID(Object entity) {
                return idGenerator.uuid();
            }
        };
    }
}
