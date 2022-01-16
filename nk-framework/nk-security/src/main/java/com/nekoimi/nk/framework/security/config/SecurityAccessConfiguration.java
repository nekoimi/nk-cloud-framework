package com.nekoimi.nk.framework.security.config;

import com.nekoimi.nk.framework.security.contract.SecurityConfigCustomizer;
import com.nekoimi.nk.framework.security.repository.RedisServerSecurityContextRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
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
    @ConditionalOnBean(value = RedisServerSecurityContextRepository.class, search = SearchStrategy.CURRENT)
    public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
                                                       ObjectProvider<List<SecurityConfigCustomizer>> configCustomizers,
                                                       RedisServerSecurityContextRepository securityContextRepository) {
        http.securityContextRepository(securityContextRepository);
        ListUtils.emptyIfNull(configCustomizers.getIfAvailable()).forEach(configCustomizer -> configCustomizer.customize(http));
        return http.build();
    }

}
