package com.nekoimi.nk.framework.security.handler;

import com.nekoimi.nk.framework.security.exception.RequestAccessDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/16 13:18
 *
 * 没有授权
 */
@Slf4j
@Component
public class AccessDeniedExceptionHandler implements ServerAccessDeniedHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException e) {
        log.error("拒绝访问： {}", e.getMessage());
        return Mono.error(new RequestAccessDeniedException());
    }
}
