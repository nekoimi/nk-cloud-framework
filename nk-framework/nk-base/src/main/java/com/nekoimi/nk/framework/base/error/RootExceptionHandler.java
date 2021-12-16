package com.nekoimi.nk.framework.base.error;

import com.nekoimi.nk.framework.core.error.BaseErrors;
import com.nekoimi.nk.framework.core.utils.ErrorUtils;
import com.nekoimi.nk.framework.core.contract.error.ErrorDetails;
import com.nekoimi.nk.framework.base.contract.error.ErrorExceptionHandler;
import com.nekoimi.nk.framework.core.protocol.ErrorDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/7/21 下午3:47
 */
@Slf4j
@Component
public class RootExceptionHandler implements ErrorExceptionHandler<Exception> {
    private static final ErrorDetails error = BaseErrors.DEFAULT_SERVER_ERROR;

    @Override
    public Class<Exception> getType() {
        return Exception.class;
    }

    @Override
    public Mono<? extends ErrorDetails> handle(ServerWebExchange exchange, Exception e) {
        log.warn("root exception handler -- \n");
        log.error(e.getMessage(), e);
        if (log.isDebugEnabled()) {
            e.printStackTrace();
        }
        return Mono.fromCallable(() -> ErrorDetailsImpl.of(error.code(), error.message(),
                ErrorUtils.getStackTrace(e), ClassUtils.getName(e))
        );
    }
}
