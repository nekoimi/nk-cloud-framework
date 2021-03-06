package com.nekoimi.nk.framework.core.exception.http;

import com.nekoimi.nk.framework.core.error.BaseErrors;
import com.nekoimi.nk.framework.core.exception.BaseRuntimeException;

/**
 * nekoimi  2021/12/10 17:57
 */
public class RequestValidationException extends BaseRuntimeException {
    public RequestValidationException() {
        super(BaseErrors.HTTP_STATUS_BAD_REQUEST);
    }

    public RequestValidationException(String message, Object... args) {
        super(BaseErrors.HTTP_STATUS_BAD_REQUEST, message, args);
    }
}
