package com.nekoimi.nk.framework.base.error.handler;

import com.nekoimi.nk.framework.base.contract.error.ErrorExceptionHandler;
import com.nekoimi.nk.framework.core.contract.error.ErrorDetails;
import com.nekoimi.nk.framework.core.exception.BaseRuntimeException;
import com.nekoimi.nk.framework.core.protocol.ErrorDetailsImpl;
import com.nekoimi.nk.framework.core.utils.ErrorUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/7/28 上午9:06
 * <p>
 * 自定义的异常处理
 */
@Slf4j
@Component
public class BaseRuntimeExceptionHandler implements ErrorExceptionHandler<BaseRuntimeException> {
    @Override
    public Class<BaseRuntimeException> getType() {
        return BaseRuntimeException.class;
    }

    @Override
    public Mono<? extends ErrorDetails> handle(ServerWebExchange exchange, BaseRuntimeException e) {
        var error = e.getError();
        return Mono.fromCallable(() -> ErrorDetailsImpl.of(error.code(), error.message(), e.getMessage(), ErrorUtils.getStackTrace(e))
        );
    }

}
