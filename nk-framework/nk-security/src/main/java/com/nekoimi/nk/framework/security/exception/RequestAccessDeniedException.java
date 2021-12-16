package com.nekoimi.nk.framework.security.exception;

import com.nekoimi.nk.framework.core.error.BaseErrors;
import com.nekoimi.nk.framework.core.exception.BaseRuntimeException;

/**
 * nekoimi  2021/12/16 19:25
 */
public class RequestAccessDeniedException extends BaseRuntimeException {
    public RequestAccessDeniedException() {
        super(BaseErrors.ACCESS_DENIED_EXCEPTION);
    }
}
