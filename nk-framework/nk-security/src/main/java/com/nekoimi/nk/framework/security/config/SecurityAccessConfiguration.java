package com.nekoimi.nk.framework.security.config;

import com.nekoimi.nk.framework.security.contract.SecurityConfigCustomizer;
import com.nekoimi.nk.framework.security.repository.RedisServerSecurityContextRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.List;

/**
 * nekoimi  2021/12/16 17:50
 * <p>
 * 安全访问配置
 * 只提供权限验证功能，不提供登录、发token
 */
@Slf4j
public class SecurityAccessConfiguration {

    @Bean
    public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
                                                       List<SecurityConfigCustomizer> configCustomizers,
                                                       RedisServerSecurityContextRepository securityContextRepository) {
        http.securityContextRepository(securityContextRepository);
        configCustomizers.forEach(configCustomizer -> configCustomizer.customize(http));
        return http.build();
    }

}
