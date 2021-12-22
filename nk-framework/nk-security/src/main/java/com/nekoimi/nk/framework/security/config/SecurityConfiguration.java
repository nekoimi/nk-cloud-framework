package com.nekoimi.nk.framework.security.config;

import com.nekoimi.nk.framework.cache.contract.CacheService;
import com.nekoimi.nk.framework.core.constant.SecurityConstants;
import com.nekoimi.nk.framework.security.config.properties.SecurityProperties;
import com.nekoimi.nk.framework.security.contract.SecurityAuthorizeExchangeCustomizer;
import com.nekoimi.nk.framework.security.contract.SecurityConfigCustomizer;
import com.nekoimi.nk.framework.security.filter.RequestParseAuthTypeFilter;
import com.nekoimi.nk.framework.security.repository.RedisServerSecurityContextRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

import java.util.List;

/**
 * nekoimi  2021/12/16 17:50
 */
@Slf4j
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {
    private final SecurityProperties properties;
    private final ServerAccessDeniedHandler accessDeniedHandler;
    private final ServerAuthenticationEntryPoint authenticationExceptionHandler;
    private final ServerAuthenticationSuccessHandler authenticationSuccessHandler;
    private final ServerAuthenticationFailureHandler authenticationFailureHandler;
    private final ServerLogoutSuccessHandler logoutSuccessHandler;
    private final ReactiveAuthenticationManager integratedAuthenticationManager;
    private final ServerAuthenticationConverter integratedToAuthenticationTokenConverterManager;

    public SecurityConfiguration(SecurityProperties properties,
                                 ServerAccessDeniedHandler accessDeniedHandler,
                                 ServerAuthenticationEntryPoint authenticationExceptionHandler,
                                 ServerAuthenticationSuccessHandler authenticationSuccessHandler,
                                 ServerAuthenticationFailureHandler authenticationFailureHandler,
                                 ServerLogoutSuccessHandler logoutSuccessHandler,
                                 ReactiveAuthenticationManager integratedAuthenticationManager,
                                 ServerAuthenticationConverter integratedToAuthenticationTokenConverterManager) {
        this.properties = properties;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationExceptionHandler = authenticationExceptionHandler;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.integratedAuthenticationManager = integratedAuthenticationManager;
        this.integratedToAuthenticationTokenConverterManager = integratedToAuthenticationTokenConverterManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(30);
    }

    @Bean
    @ConditionalOnBean(value = CacheService.class, search = SearchStrategy.CURRENT)
    public ServerSecurityContextRepository securityContextRepository(CacheService cacheService) {
        return new RedisServerSecurityContextRepository(cacheService);
    }

    @Bean
    @ConditionalOnBean(value = CacheService.class, search = SearchStrategy.CURRENT)
    public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
                                                       ServerSecurityContextRepository securityContextRepository,
                                                       List<SecurityConfigCustomizer> configCustomizers,
                                                       List<SecurityAuthorizeExchangeCustomizer> authorizeExchangeCustomizers) {
        ServerWebExchangeMatcher loginExchangeMatcher = ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, properties.getLoginUrl());
        ServerWebExchangeMatcher logoutExchangeMatcher = ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, properties.getLogoutUrl());
        // 登录认证
        http.formLogin(login -> login.requiresAuthenticationMatcher(loginExchangeMatcher)
                .authenticationSuccessHandler(authenticationSuccessHandler)
                .authenticationFailureHandler(authenticationFailureHandler)
                .authenticationManager(integratedAuthenticationManager)
                .securityContextRepository(securityContextRepository))
                // 注销认证
                .logout(logout -> logout.requiresLogout(logoutExchangeMatcher)
                        .logoutSuccessHandler(logoutSuccessHandler)
                )
                // 关闭csrf
                .csrf().disable()
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
                .exceptionHandling(handler -> handler.accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationExceptionHandler)
                )
                // 配置请求过滤
                .authorizeExchange(exchange -> {
                    exchange.pathMatchers(properties.getLoginUrl()).permitAll()
                            .pathMatchers(properties.getLogoutUrl()).permitAll();
                    SecurityConstants.getDefaultPermitAll().forEach(path -> exchange.pathMatchers(path).permitAll());
                    SecurityProperties.PathMatcher pathMatcher = properties.getMatcher();
                    if (!pathMatcher.getPermitAll().isEmpty()) {
                        pathMatcher.getPermitAll().forEach(path -> exchange.pathMatchers(path).permitAll());
                    }
                    if (!pathMatcher.getAuthenticated().isEmpty()) {
                        pathMatcher.getAuthenticated().forEach(path -> exchange.pathMatchers(path).authenticated());
                    }
                    authorizeExchangeCustomizers.forEach(exchangeCustomizer -> exchangeCustomizer.customize(exchange));
                    exchange.anyExchange().authenticated();
                })
                // 插入认证类型解析filter
                .addFilterBefore(new RequestParseAuthTypeFilter(loginExchangeMatcher), SecurityWebFiltersOrder.HTTP_BASIC);
        configCustomizers.forEach(configCustomizer -> configCustomizer.customize(http));
        SecurityWebFilterChain filterChain = http.build();
        // TODO 综合认证Token解析器
        filterChain.getWebFilters()
                .filter(webFilter -> webFilter instanceof AuthenticationWebFilter)
                .cast(AuthenticationWebFilter.class)
                .subscribe(filter -> filter.setServerAuthenticationConverter(integratedToAuthenticationTokenConverterManager));

        return filterChain;
    }

}
