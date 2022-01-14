package com.nekoimi.nk.framework.security.customizer;

import com.nekoimi.nk.framework.security.contract.SecurityConfigCustomizer;
import com.nekoimi.nk.framework.security.filter.ResolverAuthTypeParameterFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
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
    private final ServerAuthenticationSuccessHandler authenticationSuccessHandler;
    private final ServerAuthenticationFailureHandler authenticationFailureHandler;
    private final ServerLogoutSuccessHandler logoutSuccessHandler;
    private final ReactiveAuthenticationManager authenticationManager;
    private final ServerSecurityContextRepository securityContextRepository;
    private final WebFilter resolverAuthTypeParameterFilter;

    public LoginSecurityConfigCustomizer(String loginPath,
                                         String logoutPath,
                                         ServerAuthenticationSuccessHandler authenticationSuccessHandler,
                                         ServerAuthenticationFailureHandler authenticationFailureHandler,
                                         ServerLogoutSuccessHandler logoutSuccessHandler,
                                         ReactiveAuthenticationManager authenticationManager,
                                         ServerSecurityContextRepository securityContextRepository) {
        this.loginPath = loginPath;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.authenticationManager = authenticationManager;
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
                        .authenticationManager(authenticationManager)
                        .securityContextRepository(securityContextRepository))
                // 注销认证
                .logout(logout -> logout.requiresLogout(logoutExchangeMatcher).logoutSuccessHandler(logoutSuccessHandler))
                .authorizeExchange(exchange -> exchange.pathMatchers(loginPath).permitAll())
        // 插入认证类型解析filter
        .addFilterBefore(resolverAuthTypeParameterFilter, SecurityWebFiltersOrder.HTTP_BASIC);
        log.debug("login security config customizer done ...");
    }
}
