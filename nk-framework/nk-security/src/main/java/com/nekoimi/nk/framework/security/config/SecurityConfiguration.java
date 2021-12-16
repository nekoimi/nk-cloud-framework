package com.nekoimi.nk.framework.security.config;

import com.nekoimi.nk.framework.security.config.properties.SecurityProperties;
import com.nekoimi.nk.framework.security.contract.AuthenticationTokenToResultConverter;
import com.nekoimi.nk.framework.security.converter.ServerAllAuthenticationConverterManager;
import com.nekoimi.nk.framework.security.handler.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
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
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * nekoimi  2021/12/16 17:50
 */
@Slf4j
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@AllArgsConstructor
public class SecurityConfiguration {
    private final SecurityProperties properties;
    private final ServerAccessDeniedHandler accessDeniedHandler;
    private final ServerAuthenticationEntryPoint authenticationExceptionHandler;
    private final ServerAuthenticationSuccessHandler authenticationSuccessHandler;
    private final ServerAuthenticationFailureHandler authenticationFailureHandler;
    private final ServerLogoutSuccessHandler logoutSuccessHandler;
    private final ServerAllAuthenticationConverterManager allAuthenticationConverter;
    private final ReactiveAuthenticationManagerResolver<ServerWebExchange> allAuthenticationResolverManager;

    @Bean
    public ReactiveUserDetailsService demoUserDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        return new MapReactiveUserDetailsService(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(30);
    }

    @Bean
    public ServerAccessDeniedHandler accessDeniedExceptionHandler() {
        return new AccessDeniedExceptionHandler();
    }

    @Bean
    public ServerAuthenticationEntryPoint authenticationExceptionHandler() {
        return new AuthenticationExceptionHandler();
    }

    @Bean
    public ServerAuthenticationSuccessHandler authenticationSuccessHandler(List<AuthenticationTokenToResultConverter> converters) {
        return new AuthenticationSuccessHandler(converters);
    }

    @Bean
    public ServerAuthenticationFailureHandler authenticationFailureHandler() {
        return new AuthenticationFailureHandler();
    }

    @Bean
    public ServerLogoutSuccessHandler logoutSuccessHandler() {
        return new LogoutSuccessHandler();
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
                                                       CorsConfigurationSource corsConfigurationSource,
                                                       ReactiveClientRegistrationRepository clientRegistrationRepository,
                                                       ServerSecurityContextRepository securityContextRepository) {
        ServerWebExchangeMatcher loginExchangeMatcher = ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, properties.getLoginUrl());
        SecurityWebFilterChain filterChain = http
                // 登录认证
                .formLogin(login -> login.requiresAuthenticationMatcher(loginExchangeMatcher)
                        .authenticationSuccessHandler(authenticationSuccessHandler)
                        .authenticationFailureHandler(authenticationFailureHandler)
//                        .authenticationManager(new JwtReactiveAuthenticationManager(jwtDecoder))
                        .securityContextRepository(securityContextRepository))
                // 注销认证
                .logout(logout -> {
                    logout.requiresLogout(ServerWebExchangeMatchers
                            .pathMatchers(HttpMethod.POST, properties.getLogoutUrl()))
                            .logoutSuccessHandler(logoutSuccessHandler);
                })
                // oauth2
                .oauth2Login(oauth2Login -> {
                    oauth2Login.clientRegistrationRepository(clientRegistrationRepository);
                })
                .oauth2Client(oauth2Client -> {
                    oauth2Client.clientRegistrationRepository(clientRegistrationRepository);
                })
                .oauth2ResourceServer(oauth2Server -> {
                    oauth2Server.jwt();
                })
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
                // 配置cors跨域
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
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
                            .pathMatchers(properties.getMatcher().getPermitAll().toArray(new String[0])).permitAll()
                            .pathMatchers(properties.getMatcher().getAuthenticated().toArray(new String[0]))
                            .authenticated();
                })
                // 插入filter
//                .addFilterBefore(new BeforeRequestFilter(), SecurityWebFiltersOrder.CSRF)
                // build
                .build();

        filterChain.getWebFilters().filter(webFilter -> webFilter instanceof AuthenticationWebFilter)
                .subscribe(webFilter -> log.debug(webFilter.toString()));

        // TODO 综合认证管理器
        Iterator<WebFilter> iterator = filterChain.getWebFilters().toIterable().iterator();
        while (iterator.hasNext()) {
            WebFilter webFilter = iterator.next();
            if (webFilter instanceof AuthenticationWebFilter) {
                webFilter = new AuthenticationWebFilter(allAuthenticationResolverManager);
                ((AuthenticationWebFilter) webFilter).setRequiresAuthenticationMatcher(loginExchangeMatcher);
                ((AuthenticationWebFilter) webFilter).setAuthenticationFailureHandler(authenticationFailureHandler);
                ((AuthenticationWebFilter) webFilter).setServerAuthenticationConverter(allAuthenticationConverter);
                ((AuthenticationWebFilter) webFilter).setAuthenticationSuccessHandler(authenticationSuccessHandler);
                ((AuthenticationWebFilter) webFilter).setSecurityContextRepository(securityContextRepository);
            }
        }

        filterChain.getWebFilters().filter(webFilter -> webFilter instanceof AuthenticationWebFilter)
                .subscribe(webFilter -> log.debug(webFilter.toString()));

        return filterChain;
    }

}
