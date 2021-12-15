package com.nekoimi.nk.framework.base.error;

import com.nekoimi.nk.framework.base.error.enums.BaseErrors;
import com.nekoimi.nk.framework.base.util.ErrorUtils;
import com.nekoimi.nk.framework.core.error.ErrorContract;
import com.nekoimi.nk.framework.core.error.ErrorExceptionHandler;
import com.nekoimi.nk.framework.core.protocol.ErrorBody;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/7/21 下午3:46
 */
@Slf4j
@Component
public class RootErrorHandler implements ErrorExceptionHandler<Error> {
    private static final ErrorContract error = BaseErrors.DEFAULT_SERVER_ERROR;

    @Override
    public Class<Error> getType() {
        return Error.class;
    }

    @Override
    public Mono<? extends ErrorContract> handle(ServerWebExchange exchange, Error e) {
        log.warn("root error handler -- \n");
        log.error(e.getMessage(), e);
        if (log.isDebugEnabled()) {
            e.printStackTrace();
        }
        return Mono.fromCallable(() -> ErrorBody.of(error.code(), error.message(),
                ErrorUtils.getStackTrace(e), ClassUtils.getName(e))
        );
    }
}
