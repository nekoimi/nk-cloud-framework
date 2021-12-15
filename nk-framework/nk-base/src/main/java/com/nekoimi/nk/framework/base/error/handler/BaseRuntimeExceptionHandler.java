package com.nekoimi.nk.framework.base.error.handler;

import com.nekoimi.nk.framework.base.util.ErrorUtils;
import com.nekoimi.nk.framework.core.error.ErrorContract;
import com.nekoimi.nk.framework.core.error.ErrorExceptionHandler;
import com.nekoimi.nk.framework.core.exception.NkRuntimeException;
import com.nekoimi.nk.framework.core.protocol.ErrorBody;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
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
public class BaseRuntimeExceptionHandler implements ErrorExceptionHandler<NkRuntimeException> {
    @Override
    public Class<NkRuntimeException> getType() {
        return NkRuntimeException.class;
    }

    @Override
    public Mono<? extends ErrorContract> handle(ServerWebExchange exchange, NkRuntimeException e) {
        var error = e.getError();
        return Mono.fromCallable(() -> ErrorBody.of(error.code(), error.message(),
                ErrorUtils.getStackTrace(e), ClassUtils.getName(e))
        );
    }

}
