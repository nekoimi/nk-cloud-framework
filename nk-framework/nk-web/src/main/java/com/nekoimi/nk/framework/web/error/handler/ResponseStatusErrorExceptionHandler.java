package com.nekoimi.nk.framework.web.error.handler;

import com.nekoimi.nk.framework.core.contract.error.ErrorDetails;
import com.nekoimi.nk.framework.core.error.BaseErrors;
import com.nekoimi.nk.framework.core.protocol.ErrorDetailsImpl;
import com.nekoimi.nk.framework.web.contract.error.ErrorExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/7/21 下午3:34
 * <p>
 * HTTP的异常处理
 */
@Slf4j
@Component
public class ResponseStatusErrorExceptionHandler implements ErrorExceptionHandler<ResponseStatusException> {
    @Override
    public Class<ResponseStatusException> getType() {
        return ResponseStatusException.class;
    }

    @Override
    public Mono<? extends ErrorDetails> handle(ServerWebExchange exchange, ResponseStatusException e) {
        HttpStatus status = e.getStatus();
        return Mono.fromCallable(() -> {
            var error = BaseErrors.DEFAULT_SERVER_ERROR;
            if (status.is4xxClientError()) {
                error = buildHttpStatusClientError(status);
            } else if (status.is5xxServerError()) {
                error = buildHttpStatusServerError(status);
            }
            return ErrorDetailsImpl.of(error.code(), error.message(), ExceptionUtils.getStackTrace(e));
        });
    }

    private BaseErrors buildHttpStatusClientError(HttpStatus status) {
        return getClientError(status);
    }

    private BaseErrors buildHttpStatusServerError(HttpStatus status) {
        log.error("Http status server error! {}", status);
        return BaseErrors.DEFAULT_SERVER_ERROR;
    }

    public static BaseErrors getClientError(HttpStatus status) {
        switch (status) {
            case BAD_REQUEST:
                return BaseErrors.HTTP_STATUS_BAD_REQUEST;
            case UNAUTHORIZED:
                return BaseErrors.HTTP_STATUS_UNAUTHORIZED;
            case FORBIDDEN:
                return BaseErrors.HTTP_STATUS_FORBIDDEN;
            case NOT_FOUND:
                return BaseErrors.HTTP_STATUS_NOT_FOUND;
            case METHOD_NOT_ALLOWED:
                return BaseErrors.HTTP_STATUS_METHOD_NOT_ALLOWED;
            case NOT_ACCEPTABLE:
                return BaseErrors.HTTP_STATUS_NOT_ACCEPTABLE;
        }
        return BaseErrors.DEFAULT_CLIENT_ERROR;
    }
}
