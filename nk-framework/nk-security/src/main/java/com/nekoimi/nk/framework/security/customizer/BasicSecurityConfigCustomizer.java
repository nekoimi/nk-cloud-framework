package com.nekoimi.nk.framework.security.customizer;

import com.nekoimi.nk.framework.security.config.properties.SecurityProperties;
import com.nekoimi.nk.framework.security.contract.SecurityAuthorizeExchangeCustomizer;
import com.nekoimi.nk.framework.security.contract.SecurityConfigCustomizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;

import java.util.List;

/**
 * nekoimi  2022/1/14 20:08
 */
@Slf4j
public class BasicSecurityConfigCustomizer implements SecurityConfigCustomizer {
    private final SecurityProperties properties;
    private final ServerAccessDeniedHandler accessDeniedHandler;
    private final ServerAuthenticationEntryPoint authenticationExceptionHandler;
    private final List<SecurityAuthorizeExchangeCustomizer> authorizeExchangeCustomizers;

    public BasicSecurityConfigCustomizer(SecurityProperties properties,
                                         ServerAccessDeniedHandler accessDeniedHandler,
                                         ServerAuthenticationEntryPoint authenticationExceptionHandler,
                                         List<SecurityAuthorizeExchangeCustomizer> authorizeExchangeCustomizers) {
        this.properties = properties;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationExceptionHandler = authenticationExceptionHandler;
        this.authorizeExchangeCustomizers = authorizeExchangeCustomizers;
    }

    @Override
    public void customize(ServerHttpSecurity http) {
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
                    exchange.pathMatchers("/").permitAll();
                    if (!properties.getPermitAll().isEmpty()) {
                        properties.getPermitAll().forEach(path -> exchange.pathMatchers(path).permitAll());
                    }
                    if (!properties.getAuthenticated().isEmpty()) {
                        properties.getAuthenticated().forEach(path -> exchange.pathMatchers(path).authenticated());
                    }
                    authorizeExchangeCustomizers.forEach(exchangeCustomizer -> exchangeCustomizer.customize(exchange));
                });
        log.debug("basic security config customizer done ...");
    }
}
