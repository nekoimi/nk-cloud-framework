package com.nekoimi.nk.framework.security.config;

import com.nekoimi.nk.framework.security.config.properties.SecurityProperties;
import com.nekoimi.nk.framework.security.contract.SecurityAuthorizeExchangeCustomizer;
import com.nekoimi.nk.framework.security.contract.SecurityConfigCustomizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;

import java.util.List;

/**
 * nekoimi  2021/12/16 17:50
 * <p>
 * 安全访问配置
 * 只提供权限验证功能，不提供登录、发token
 */
@Slf4j
public class SecurityAccessConfiguration {
    private final SecurityProperties properties;
    private final ServerAccessDeniedHandler accessDeniedHandler;
    private final ServerAuthenticationEntryPoint authenticationExceptionHandler;

    public SecurityAccessConfiguration(SecurityProperties properties,
                                       ServerAccessDeniedHandler accessDeniedHandler,
                                       ServerAuthenticationEntryPoint authenticationExceptionHandler) {
        this.properties = properties;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationExceptionHandler = authenticationExceptionHandler;
    }

    @Bean
    public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
                                                       List<SecurityConfigCustomizer> configCustomizers,
                                                       List<SecurityAuthorizeExchangeCustomizer> authorizeExchangeCustomizers) {
        // 关闭csrf
        http.csrf().disable()
                // 关闭匿名用户
                .anonymous().disable()
                // 关闭Basic基础认证
                .httpBasic().disable()
                // 关闭乱七八杂的header
                .headers().disable()
                // 关闭缓存
                .requestCache(ServerHttpSecurity.RequestCacheSpec::disable)
                // disable cors
                .cors().disable()
                // 异常处理
                .exceptionHandling(handler -> handler.accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(authenticationExceptionHandler))
                // 配置请求过滤
                .authorizeExchange(exchange -> {
                    if (!properties.getPermitAll().isEmpty()) {
                        properties.getPermitAll().forEach(path -> exchange.pathMatchers(path).permitAll());
                    }
                    if (!properties.getAuthenticated().isEmpty()) {
                        properties.getAuthenticated().forEach(path -> exchange.pathMatchers(path).authenticated());
                    }
                    authorizeExchangeCustomizers.forEach(exchangeCustomizer -> exchangeCustomizer.customize(exchange));
                    exchange.anyExchange().authenticated();
                });
        configCustomizers.forEach(configCustomizer -> configCustomizer.customize(http));
        return http.build();
    }

}
