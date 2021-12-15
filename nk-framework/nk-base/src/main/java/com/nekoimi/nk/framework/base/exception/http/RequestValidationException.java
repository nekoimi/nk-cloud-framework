package com.nekoimi.nk.framework.base.exception.http;

import com.nekoimi.nk.framework.core.error.ErrorContract;
import com.nekoimi.nk.framework.core.exception.NkRuntimeException;

/**
 * nekoimi  2021/12/10 17:57
 */
public class RequestValidationException extends NkRuntimeException {
    public RequestValidationException(ErrorContract error) {
        super(error);
    }
}
