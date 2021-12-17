package com.nekoimi.nk.framework.security.handler;

import com.nekoimi.nk.framework.security.exception.RequestAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/16 14:06
 */
@Slf4j
@Component
public class AuthenticationFailureHandler implements ServerAuthenticationFailureHandler {
    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange filterExchange, AuthenticationException e) {
        log.error("认证失败: {}", e.getMessage());
        return Mono.error(new RequestAuthenticationException(e.getMessage()));
    }
}
