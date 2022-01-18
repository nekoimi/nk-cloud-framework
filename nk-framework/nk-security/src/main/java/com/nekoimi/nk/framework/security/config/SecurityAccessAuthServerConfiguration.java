package com.nekoimi.nk.framework.security.config;

import com.nekoimi.nk.framework.security.contract.LoginMappingController;
import com.nekoimi.nk.framework.security.contract.LogoutMappingController;
import com.nekoimi.nk.framework.security.contract.SecurityConfigCustomizer;
import com.nekoimi.nk.framework.security.customizer.LoginSecurityConfigCustomizer;
import com.nekoimi.nk.framework.security.factory.AuthenticationManagerFactory;
import com.nekoimi.nk.framework.security.handler.AuthenticationFailureHandler;
import com.nekoimi.nk.framework.security.handler.AuthenticationSuccessHandler;
import com.nekoimi.nk.framework.security.handler.LogoutSuccessHandler;
import com.nekoimi.nk.framework.security.repository.RedisServerSecurityContextRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

import java.util.List;

/**
 * nekoimi  2022/1/12 20:35
 * <p>
 * 安全认证服务器配置
 */
@Slf4j
public class SecurityAccessAuthServerConfiguration {
    private final AuthenticationManagerFactory authenticationManagerFactory;

    public SecurityAccessAuthServerConfiguration(AuthenticationManagerFactory authenticationManagerFactory) {
        this.authenticationManagerFactory = authenticationManagerFactory;
    }

    @Bean
    @ConditionalOnBean(value = LoginMappingController.class, search = SearchStrategy.CURRENT)
    public LoginMappingController SimpleLoginMappingController() {
        return () -> ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/login");
    }

    @Bean
    @ConditionalOnBean(value = LogoutMappingController.class, search = SearchStrategy.CURRENT)
    public LogoutMappingController SimpleLogoutMappingController() {
        return () -> ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/logout");
    }

    @Bean
    @ConditionalOnBean(value = RedisServerSecurityContextRepository.class, search = SearchStrategy.CURRENT)
    public LoginSecurityConfigCustomizer loginSecurityConfigCustomizer(LoginMappingController loginMappingController,
                                                                       LogoutMappingController logoutMappingController,
                                                                       AuthenticationSuccessHandler authenticationSuccessHandler,
                                                                       AuthenticationFailureHandler authenticationFailureHandler,
                                                                       LogoutSuccessHandler logoutSuccessHandler,
                                                                       RedisServerSecurityContextRepository securityContextRepository) {
        return new LoginSecurityConfigCustomizer(
                loginMappingController,
                logoutMappingController,
                authenticationSuccessHandler,
                authenticationFailureHandler,
                logoutSuccessHandler,
                authenticationManagerFactory,
                securityContextRepository
        );
    }

    @Bean
    @ConditionalOnBean(value = RedisServerSecurityContextRepository.class, search = SearchStrategy.CURRENT)
    public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
                                                       ObjectProvider<List<SecurityConfigCustomizer>> configCustomizers,
                                                       RedisServerSecurityContextRepository securityContextRepository) {
        http.securityContextRepository(securityContextRepository);
        ListUtils.emptyIfNull(configCustomizers.getIfAvailable()).forEach(configCustomizer -> configCustomizer.customize(http));
        // 使用综合认证Token解析器
        return replaceServerAuthenticationConverter(http.build());
    }

    private SecurityWebFilterChain replaceServerAuthenticationConverter(SecurityWebFilterChain filterChain) {
        filterChain.getWebFilters()
                .filter(webFilter -> webFilter instanceof AuthenticationWebFilter)
                .cast(AuthenticationWebFilter.class)
                .subscribe(filter -> filter.setServerAuthenticationConverter(authenticationManagerFactory));
        return filterChain;
    }
}
