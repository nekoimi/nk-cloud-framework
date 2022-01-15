package com.nekoimi.nk.framework.security.customizer;

import com.nekoimi.nk.framework.security.contract.SecurityConfigCustomizer;
import com.nekoimi.nk.framework.security.factory.AuthenticationManagerFactory;
import com.nekoimi.nk.framework.security.filter.ResolverAuthTypeParameterFilter;
import com.nekoimi.nk.framework.security.handler.AuthenticationFailureHandler;
import com.nekoimi.nk.framework.security.handler.AuthenticationSuccessHandler;
import com.nekoimi.nk.framework.security.handler.LogoutSuccessHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.WebFilter;

/**
 * nekoimi  2022/1/12 20:52
 * <p>
 * 认证入口配置
 */
@Slf4j
public class LoginSecurityConfigCustomizer implements SecurityConfigCustomizer {
    private final String loginPath;
    private final ServerWebExchangeMatcher loginExchangeMatcher;
    private final ServerWebExchangeMatcher logoutExchangeMatcher;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final LogoutSuccessHandler logoutSuccessHandler;
    private final AuthenticationManagerFactory authenticationManagerFactory;
    private final ServerSecurityContextRepository securityContextRepository;
    private final WebFilter resolverAuthTypeParameterFilter;

    public LoginSecurityConfigCustomizer(String loginPath,
                                         String logoutPath,
                                         AuthenticationSuccessHandler authenticationSuccessHandler,
                                         AuthenticationFailureHandler authenticationFailureHandler,
                                         LogoutSuccessHandler logoutSuccessHandler,
                                         AuthenticationManagerFactory authenticationManagerFactory,
                                         ServerSecurityContextRepository securityContextRepository) {
        this.loginPath = loginPath;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.authenticationManagerFactory = authenticationManagerFactory;
        this.securityContextRepository = securityContextRepository;
        this.loginExchangeMatcher = ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, loginPath);
        this.logoutExchangeMatcher = ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, logoutPath);
        this.resolverAuthTypeParameterFilter = new ResolverAuthTypeParameterFilter(loginExchangeMatcher);
    }

    @Override
    public void customize(ServerHttpSecurity http) {
        http
                // 登录认证
                .formLogin(login -> login.requiresAuthenticationMatcher(loginExchangeMatcher)
                        .authenticationSuccessHandler(authenticationSuccessHandler)
                        .authenticationFailureHandler(authenticationFailureHandler)
                        .authenticationManager(authenticationManagerFactory)
                        .securityContextRepository(securityContextRepository))
                // 注销认证
                .logout(logout -> logout.requiresLogout(logoutExchangeMatcher).logoutSuccessHandler(logoutSuccessHandler))
                .authorizeExchange(exchange -> exchange.pathMatchers(HttpMethod.POST, loginPath).permitAll())
        // 插入认证类型解析filter
        .addFilterBefore(resolverAuthTypeParameterFilter, SecurityWebFiltersOrder.HTTP_BASIC);
        log.debug("login security config customizer done ...");
    }
}
