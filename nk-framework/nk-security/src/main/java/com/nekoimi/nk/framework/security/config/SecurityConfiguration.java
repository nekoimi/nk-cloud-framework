package com.nekoimi.nk.framework.security.config;

import com.nekoimi.nk.framework.security.config.properties.SecurityProperties;
import com.nekoimi.nk.framework.security.filter.RequestParseAuthTypeFilter;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

import java.util.UUID;

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
    public ServerSecurityContextRepository securityContextRepository() {
        return new WebSessionServerSecurityContextRepository();
    }

    @Bean
    public ReactiveClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration loginClient = ClientRegistration.withRegistrationId(UUID.randomUUID().toString())
                .clientId("login-client")
                .clientSecret("{noop}openid-connect")
                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUriTemplate("http://127.0.0.1:8080/login/oauth2/code/login-client")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .tokenUri("http://127.0.0.1:8080/login/oauth2/code/login-client")
                .build();
        ClientRegistration registeredClient = ClientRegistration.withRegistrationId(UUID.randomUUID().toString())
                .clientId("messaging-client")
                .clientSecret("{noop}secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scope("message:read")
                .scope("message:write")
                .tokenUri("http://127.0.0.1:8080/login/oauth2/code/login-client")
                .build();
        return new InMemoryReactiveClientRegistrationRepository(loginClient, registeredClient);
    }

    @Bean
    public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
                                                       ServerSecurityContextRepository securityContextRepository) {
        ServerWebExchangeMatcher loginExchangeMatcher = ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, properties.getLoginUrl());
        ServerWebExchangeMatcher logoutExchangeMatcher = ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, properties.getLogoutUrl());
        SecurityWebFilterChain filterChain = http
                // 登录认证
                .formLogin(login -> login.requiresAuthenticationMatcher(loginExchangeMatcher)
                        .authenticationSuccessHandler(authenticationSuccessHandler)
                        .authenticationFailureHandler(authenticationFailureHandler)
                        .authenticationManager(integratedAuthenticationManager)
                        .securityContextRepository(securityContextRepository))
                // 注销认证
                .logout(logout -> logout.requiresLogout(logoutExchangeMatcher)
                        .logoutSuccessHandler(logoutSuccessHandler))
                // oauth2
//                .oauth2Login().and()
//                .oauth2Client().and()
//                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt)
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
                .exceptionHandling(handler -> handler
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationExceptionHandler))
                // 配置请求过滤
                .authorizeExchange(exchange -> {
                    exchange.pathMatchers("/").permitAll()
                            .pathMatchers("/doc.html").permitAll()
                            .pathMatchers("/webjars/**").permitAll()
                            .pathMatchers("/v2/api-docs").permitAll()
                            .pathMatchers("/swagger-resources").permitAll()
                            .pathMatchers("/oauth2/**").permitAll()
                            .pathMatchers("/login/oauth2/**").permitAll()
                            .pathMatchers("/pub-key/jwk.json").permitAll()
                            .pathMatchers(properties.getLoginUrl()).permitAll()
                            .pathMatchers(properties.getLogoutUrl()).permitAll();
                    SecurityProperties.PathMatcher pathMatcher = properties.getMatcher();
                    if (!pathMatcher.getPermitAll().isEmpty()) {
                        pathMatcher.getPermitAll().forEach(path -> exchange.pathMatchers(path).permitAll());
                    }
                    if (!pathMatcher.getAuthenticated().isEmpty()) {
                        pathMatcher.getAuthenticated().forEach(path -> exchange.pathMatchers(path).authenticated());
                    }
                    exchange.anyExchange().authenticated();
                })
                // 插入认证类型解析filter
                .addFilterBefore(new RequestParseAuthTypeFilter(loginExchangeMatcher), SecurityWebFiltersOrder.HTTP_BASIC)
                // build
                .build();

        // TODO 综合认证Token解析器
        filterChain.getWebFilters()
                .filter(webFilter -> webFilter instanceof AuthenticationWebFilter)
                .cast(AuthenticationWebFilter.class)
                .subscribe(filter -> filter.setServerAuthenticationConverter(integratedToAuthenticationTokenConverterManager));

        return filterChain;
    }

}
