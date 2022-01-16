package com.nekoimi.nk.framework.mybatis.config;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.nekoimi.nk.framework.core.contract.IdGenerator;
import com.nekoimi.nk.framework.mybatis.injector.ExtensionSqlInjector;
import com.nekoimi.nk.framework.mybatis.plugins.OverflowPaginationInnerInterceptor;
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
        return new ExtensionSqlInjector();
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

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//        /**
//         * 多租户插件
//         */
//        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
//            @Override
//            public Expression getTenantId() {
//                return null;
//            }
//        }));

//        /**
//         * 动态表名插件
//         */
//        interceptor.addInnerInterceptor(new DynamicTableNameInnerInterceptor(new TableNameHandler() {
//            @Override
//            public String dynamicTableName(String sql, String tableName) {
//                return null;
//            }
//        }));

        /**
         * 分页插件
         */
        OverflowPaginationInnerInterceptor overflowPaginationInnerInterceptor = new OverflowPaginationInnerInterceptor();
        overflowPaginationInnerInterceptor.setMaxLimit(500L);
        overflowPaginationInnerInterceptor.setOverflow(true);
        interceptor.addInnerInterceptor(overflowPaginationInnerInterceptor);

//        /**
//         * sql 性能规范插件
//         */
//        interceptor.addInnerInterceptor(new IllegalSQLInnerInterceptor());
        return interceptor;
    }
}
