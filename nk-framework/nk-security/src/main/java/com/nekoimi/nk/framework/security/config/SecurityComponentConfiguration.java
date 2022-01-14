package com.nekoimi.nk.framework.security.config;

import com.nekoimi.nk.framework.redis.service.RedisService;
import com.nekoimi.nk.framework.security.config.properties.SecurityProperties;
import com.nekoimi.nk.framework.security.contract.SecurityAuthorizeExchangeCustomizer;
import com.nekoimi.nk.framework.security.customizer.BasicSecurityConfigCustomizer;
import com.nekoimi.nk.framework.security.customizer.SwaggerSecurityAuthorizeExchangeCustomizer;
import com.nekoimi.nk.framework.security.repository.CacheServerSecurityContextRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;

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
    public ServerSecurityContextRepository securityContextRepository(RedisService redisService) {
        return new CacheServerSecurityContextRepository(redisService);
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
                                                                       ServerAccessDeniedHandler accessDeniedHandler,
                                                                       ServerAuthenticationEntryPoint authenticationExceptionHandler,
                                                                       List<SecurityAuthorizeExchangeCustomizer> authorizeExchangeCustomizers) {
        return new BasicSecurityConfigCustomizer(properties, accessDeniedHandler, authenticationExceptionHandler, authorizeExchangeCustomizers);
    }
}
