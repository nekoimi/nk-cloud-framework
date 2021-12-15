package com.nekoimi.nk.framework.base.error;

import com.nekoimi.nk.framework.core.error.ErrorContract;
import com.nekoimi.nk.framework.core.error.ErrorExceptionHandlerResolver;
import com.nekoimi.nk.framework.core.error.ErrorExceptionResultWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/13 22:14
 *
 * 全局异常处理器
 */
@Slf4j
@Component
@AllArgsConstructor
public class ServerErrorHandler implements ErrorWebExceptionHandler, Ordered {
    private final ErrorExceptionResultWriter resultWriter;
    private final ErrorExceptionHandlerResolver handlerResolver;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        return handlerResolver.resolve(ex)
                .flatMap(handler -> (Mono<ErrorContract>) handler.handle(exchange, ex))
                .flatMap(error -> resultWriter.writer(exchange, error))
                .doOnError(e -> log.error("server error handler -- \n" + e.getMessage(), e));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}