package com.nekoimi.nk.framework.security.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/16 22:51
 *
 * AuthType多类型验证管理器
 * 根据Token类型选择对应的验证器
 */
@Slf4j
@Component
public class ServerAllAuthenticationManagerResolverManager implements ReactiveAuthenticationManagerResolver<ServerWebExchange> {
    @Override
    public Mono<ReactiveAuthenticationManager> resolve(ServerWebExchange context) {
        return null;
    }
}
