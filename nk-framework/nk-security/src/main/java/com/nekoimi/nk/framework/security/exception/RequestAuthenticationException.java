package com.nekoimi.nk.framework.security.exception;

import com.nekoimi.nk.framework.core.error.BaseErrors;
import com.nekoimi.nk.framework.core.exception.BaseRuntimeException;

/**
 * nekoimi  2021/12/16 19:35
 */
public class RequestAuthenticationException extends BaseRuntimeException {
    public RequestAuthenticationException() {
        super(BaseErrors.AUTHENTICATION_EXCEPTION);
    }

    public RequestAuthenticationException(String message, Object... args) {
        super(BaseErrors.AUTHENTICATION_EXCEPTION, message, args);
    }
}
