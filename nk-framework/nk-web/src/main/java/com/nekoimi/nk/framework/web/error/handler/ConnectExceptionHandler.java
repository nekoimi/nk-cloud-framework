package com.nekoimi.nk.framework.web.error.handler;

import com.nekoimi.nk.framework.core.contract.error.ErrorDetails;
import com.nekoimi.nk.framework.core.error.BaseErrors;
import com.nekoimi.nk.framework.core.protocol.ErrorDetailsImpl;
import com.nekoimi.nk.framework.core.utils.ErrorUtils;
import com.nekoimi.nk.framework.web.contract.error.ErrorExceptionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.ConnectException;

/**
 * nekoimi  2021/12/25 14:24
 */
@Component
public class ConnectExceptionHandler implements ErrorExceptionHandler<ConnectException> {
    @Override
    public Class<ConnectException> getType() {
        return ConnectException.class;
    }

    @Override
    public Mono<? extends ErrorDetails> handle(ServerWebExchange exchange, ConnectException e) {
        return Mono.fromCallable(() -> ErrorDetailsImpl.of(BaseErrors.CONNECT_EXCEPTION.code(), e.getMessage(), ErrorUtils.getStackTrace(e)));
    }
}
