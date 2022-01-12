package com.nekoimi.nk.framework.security.customizer;

import com.nekoimi.nk.framework.security.contract.SecurityConfigCustomizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;

/**
 * nekoimi  2022/1/12 20:52
 * <p>
 * 认证入口配置
 */
@Slf4j
public class LoginSecurityConfigCustomizer implements SecurityConfigCustomizer {
    private final ServerWebExchangeMatcher loginExchangeMatcher;
    private final ServerWebExchangeMatcher logoutExchangeMatcher;
    private final ServerAuthenticationSuccessHandler authenticationSuccessHandler;
    private final ServerAuthenticationFailureHandler authenticationFailureHandler;
    private final ServerLogoutSuccessHandler logoutSuccessHandler;
    private final ReactiveAuthenticationManager integratedAuthenticationManager;
    private final ServerSecurityContextRepository securityContextRepository;

    public LoginSecurityConfigCustomizer(ServerWebExchangeMatcher loginExchangeMatcher,
                                         ServerWebExchangeMatcher logoutExchangeMatcher,
                                         ServerAuthenticationSuccessHandler authenticationSuccessHandler,
                                         ServerAuthenticationFailureHandler authenticationFailureHandler,
                                         ServerLogoutSuccessHandler logoutSuccessHandler,
                                         ReactiveAuthenticationManager integratedAuthenticationManager,
                                         ServerSecurityContextRepository securityContextRepository) {
        this.loginExchangeMatcher = loginExchangeMatcher;
        this.logoutExchangeMatcher = logoutExchangeMatcher;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.integratedAuthenticationManager = integratedAuthenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @Override
    public void customize(ServerHttpSecurity http) {
        http
                // 登录认证
                .formLogin(login -> login.requiresAuthenticationMatcher(loginExchangeMatcher)
                .authenticationSuccessHandler(authenticationSuccessHandler)
                .authenticationFailureHandler(authenticationFailureHandler)
                .authenticationManager(integratedAuthenticationManager)
                .securityContextRepository(securityContextRepository))
                // 注销认证
                .logout(logout -> logout.requiresLogout(logoutExchangeMatcher)
                        .logoutSuccessHandler(logoutSuccessHandler));
    }
}
