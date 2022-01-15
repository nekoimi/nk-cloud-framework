package com.nekoimi.nk.framework.security.config;

import com.nekoimi.nk.framework.cache.contract.RedisService;
import com.nekoimi.nk.framework.security.config.properties.SecurityProperties;
import com.nekoimi.nk.framework.security.contract.SecurityAuthorizeExchangeCustomizer;
import com.nekoimi.nk.framework.security.customizer.BasicSecurityConfigCustomizer;
import com.nekoimi.nk.framework.security.customizer.SwaggerSecurityAuthorizeExchangeCustomizer;
import com.nekoimi.nk.framework.security.handler.AccessDeniedExceptionHandler;
import com.nekoimi.nk.framework.security.handler.AuthenticationExceptionHandler;
import com.nekoimi.nk.framework.security.repository.RedisServerSecurityContextRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * nekoimi  2022/1/12 20:41
 */

public class SecurityComponentConfiguration {

    /**
     * 安全上下文存储
     *
     * @param redisService
     * @return
     */
    @Bean
    @ConditionalOnBean(value = RedisService.class, search = SearchStrategy.CURRENT)
    public RedisServerSecurityContextRepository securityContextRepository(RedisService redisService) {
        return new RedisServerSecurityContextRepository(redisService);
    }

    /**
     * Swagger 路径安全访问
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "app.web.swagger", name = "enabled", havingValue = "true")
    public SwaggerSecurityAuthorizeExchangeCustomizer swaggerSecurityAuthorizeExchangeCustomizer() {
        return new SwaggerSecurityAuthorizeExchangeCustomizer();
    }

    /**
     * @param properties
     * @param accessDeniedHandler
     * @param authenticationExceptionHandler
     * @param authorizeExchangeCustomizers
     * @return
     */
    @Bean
    public BasicSecurityConfigCustomizer basicSecurityConfigCustomizer(SecurityProperties properties,
                                                                       AccessDeniedExceptionHandler accessDeniedHandler,
                                                                       AuthenticationExceptionHandler authenticationExceptionHandler,
                                                                       List<SecurityAuthorizeExchangeCustomizer> authorizeExchangeCustomizers) {
        return new BasicSecurityConfigCustomizer(properties, accessDeniedHandler, authenticationExceptionHandler, authorizeExchangeCustomizers);
    }
}
