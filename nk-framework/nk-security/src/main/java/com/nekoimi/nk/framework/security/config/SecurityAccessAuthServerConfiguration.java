package com.nekoimi.nk.framework.security.config;

import com.nekoimi.nk.framework.cache.contract.CacheService;
import com.nekoimi.nk.framework.security.config.properties.SecurityProperties;
import com.nekoimi.nk.framework.security.filter.ResolverAuthTypeParameterFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.WebFilter;

/**
 * nekoimi  2022/1/12 20:35
 * <p>
 * 安全认证服务器配置
 */
@Slf4j
public class SecurityAccessAuthServerConfiguration {
    private final SecurityProperties properties;
    private final ServerAuthenticationSuccessHandler authenticationSuccessHandler;
    private final ServerAuthenticationFailureHandler authenticationFailureHandler;
    private final ServerLogoutSuccessHandler logoutSuccessHandler;
    private final ReactiveAuthenticationManager integratedAuthenticationManager;
    private final ServerAuthenticationConverter integratedToAuthenticationTokenConverterManager;
    private final ServerWebExchangeMatcher loginExchangeMatcher;
    private final ServerWebExchangeMatcher logoutExchangeMatcher;
    private final WebFilter resolverAuthTypeParameterFilter;

    public SecurityAccessAuthServerConfiguration(SecurityProperties properties,
                                                 ServerAuthenticationSuccessHandler authenticationSuccessHandler,
                                                 ServerAuthenticationFailureHandler authenticationFailureHandler,
                                                 ServerLogoutSuccessHandler logoutSuccessHandler,
                                                 ReactiveAuthenticationManager integratedAuthenticationManager,
                                                 ServerAuthenticationConverter integratedToAuthenticationTokenConverterManager) {
        this.properties = properties;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.integratedAuthenticationManager = integratedAuthenticationManager;
        this.integratedToAuthenticationTokenConverterManager = integratedToAuthenticationTokenConverterManager;
        this.loginExchangeMatcher = ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, properties.getLoginPath());
        this.logoutExchangeMatcher = ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, properties.getLogoutPath());
        this.resolverAuthTypeParameterFilter = new ResolverAuthTypeParameterFilter(loginExchangeMatcher);
    }

    private SecurityWebFilterChain replaceServerAuthenticationConverter(SecurityWebFilterChain filterChain) {
        filterChain.getWebFilters()
                .filter(webFilter -> webFilter instanceof AuthenticationWebFilter)
                .cast(AuthenticationWebFilter.class)
                .subscribe(filter -> filter.setServerAuthenticationConverter(integratedToAuthenticationTokenConverterManager));
        return filterChain;
    }

    @Bean
    @ConditionalOnClass(value = CacheService.class)
    @ConditionalOnBean(value = CacheService.class, search = SearchStrategy.CURRENT)
    public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
                                                       ServerSecurityContextRepository securityContextRepository) {
        // 登录认证
        http.formLogin(login -> login.requiresAuthenticationMatcher(loginExchangeMatcher)
                .authenticationSuccessHandler(authenticationSuccessHandler)
                .authenticationFailureHandler(authenticationFailureHandler)
                .authenticationManager(integratedAuthenticationManager)
                .securityContextRepository(securityContextRepository))
                // 注销认证
                .logout(logout -> logout.requiresLogout(logoutExchangeMatcher).logoutSuccessHandler(logoutSuccessHandler))
                // 配置请求过滤
                .authorizeExchange(exchange -> {
                    exchange.pathMatchers("/").permitAll();
                    exchange.pathMatchers(properties.getLoginPath()).permitAll();
                    exchange.pathMatchers(properties.getLogoutPath()).permitAll();
                })
                // 插入认证类型解析filter
                .addFilterBefore(resolverAuthTypeParameterFilter, SecurityWebFiltersOrder.HTTP_BASIC);
        // TODO 综合认证Token解析器
        return replaceServerAuthenticationConverter(http.build());
    }
}
