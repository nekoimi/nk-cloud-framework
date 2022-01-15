package com.nekoimi.nk.framework.security.config;

import com.nekoimi.nk.framework.security.config.properties.SecurityProperties;
import com.nekoimi.nk.framework.security.contract.SecurityConfigCustomizer;
import com.nekoimi.nk.framework.security.customizer.LoginSecurityConfigCustomizer;
import com.nekoimi.nk.framework.security.factory.AuthenticationManagerFactory;
import com.nekoimi.nk.framework.security.handler.AuthenticationFailureHandler;
import com.nekoimi.nk.framework.security.handler.AuthenticationSuccessHandler;
import com.nekoimi.nk.framework.security.handler.LogoutSuccessHandler;
import com.nekoimi.nk.framework.security.repository.RedisServerSecurityContextRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

import java.util.List;

/**
 * nekoimi  2022/1/12 20:35
 * <p>
 * 安全认证服务器配置
 */
@Slf4j
public class SecurityAccessAuthServerConfiguration {
    private final SecurityProperties properties;
    private final AuthenticationManagerFactory authenticationManagerFactory;

    public SecurityAccessAuthServerConfiguration(SecurityProperties properties, AuthenticationManagerFactory authenticationManagerFactory) {
        this.properties = properties;
        this.authenticationManagerFactory = authenticationManagerFactory;
    }

    @Bean
    @ConditionalOnBean(value = RedisServerSecurityContextRepository.class, search = SearchStrategy.CURRENT)
    public LoginSecurityConfigCustomizer loginSecurityConfigCustomizer(AuthenticationSuccessHandler authenticationSuccessHandler,
                                                                       AuthenticationFailureHandler authenticationFailureHandler,
                                                                       LogoutSuccessHandler logoutSuccessHandler,
                                                                       RedisServerSecurityContextRepository securityContextRepository) {
        return new LoginSecurityConfigCustomizer(
                properties.getLoginPath(),
                properties.getLogoutPath(),
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
                                                       List<SecurityConfigCustomizer> configCustomizers,
                                                       RedisServerSecurityContextRepository securityContextRepository) {
        http.securityContextRepository(securityContextRepository);
        configCustomizers.forEach(configCustomizer -> configCustomizer.customize(http));
        configCustomizers.forEach(configCustomizer -> configCustomizer.customize(http));
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
