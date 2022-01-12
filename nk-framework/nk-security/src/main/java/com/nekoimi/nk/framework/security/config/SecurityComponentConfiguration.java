package com.nekoimi.nk.framework.security.config;

import com.nekoimi.nk.framework.redis.service.RedisService;
import com.nekoimi.nk.framework.security.customizer.SwaggerSecurityAuthorizeExchangeCustomizer;
import com.nekoimi.nk.framework.security.repository.CacheServerSecurityContextRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;

/**
 * nekoimi  2022/1/12 20:41
 */

public class SecurityComponentConfiguration {

    /**
     * 安全上下文存储
     * @param redisService
     * @return
     */
    @Bean
    @ConditionalOnClass(value = RedisService.class)
    @ConditionalOnBean(value = RedisService.class, search = SearchStrategy.CURRENT)
    public ServerSecurityContextRepository securityContextRepository(RedisService redisService) {
        return new CacheServerSecurityContextRepository(redisService);
    }

    /**
     * Swagger 路径安全访问
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "app.web.swagger", name = "enabled", havingValue = "true")
    public SwaggerSecurityAuthorizeExchangeCustomizer swaggerSecurityAuthorizeExchangeCustomizer() {
        return new SwaggerSecurityAuthorizeExchangeCustomizer();
    }
}