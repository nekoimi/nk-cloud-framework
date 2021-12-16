package com.nekoimi.nk.framework.security.handler;

import com.nekoimi.nk.framework.security.exception.RequestAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/16 13:21
 *
 * 没有登录认证
 */
@Slf4j
@Component
public class AuthenticationExceptionHandler implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange serverWebExchange, AuthenticationException e) {
        log.error("认证错误： {}", e.getMessage());
        return Mono.error(new RequestAuthenticationException());
    }
}
