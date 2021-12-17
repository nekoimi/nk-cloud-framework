package com.nekoimi.nk.framework.web.error;

import com.nekoimi.nk.framework.core.contract.error.ErrorDetails;
import com.nekoimi.nk.framework.web.contract.error.ErrorExceptionHandlerResolver;
import com.nekoimi.nk.framework.web.contract.error.ErrorExceptionResultWriter;
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
public class ServerErrorHandler implements ErrorWebExceptionHandler, Ordered {
    private final ErrorExceptionResultWriter resultWriter;
    private final ErrorExceptionHandlerResolver handlerResolver;

    public ServerErrorHandler(ErrorExceptionResultWriter resultWriter,
                              ErrorExceptionHandlerResolver handlerResolver) {
        this.resultWriter = resultWriter;
        this.handlerResolver = handlerResolver;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        return handlerResolver.resolve(ex)
                .flatMap(handler -> (Mono<ErrorDetails>) handler.handle(exchange, ex))
                .flatMap(error -> resultWriter.writer(exchange, error))
                .doOnError(e -> log.error("server error handler -- \n" + e.getMessage(), e));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
