package com.nekoimi.nk.framework.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/16 13:21
 *
 * 没有登录认证
 */
@Slf4j
public class JwtAuthenticationExceptionHandler implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange serverWebExchange, AuthenticationException e) {
        log.error("没有认证： {}", e.getMessage());
        return Mono.error(e);
    }
}
