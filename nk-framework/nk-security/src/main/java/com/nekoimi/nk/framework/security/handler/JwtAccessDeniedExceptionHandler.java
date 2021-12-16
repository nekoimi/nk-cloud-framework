package com.nekoimi.nk.framework.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/16 13:18
 *
 * 没有授权
 */
@Slf4j
public class JwtAccessDeniedExceptionHandler implements ServerAccessDeniedHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, AccessDeniedException e) {
        log.error("没有权限： {}", e.getMessage());
        return Mono.error(e);
    }
}
