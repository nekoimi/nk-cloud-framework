package com.nekoimi.nk.framework.core.exception;

import com.nekoimi.nk.framework.core.contract.error.ErrorDetails;

/**
 * nekoimi  2021/12/6 14:37
 */
public class BaseRuntimeException extends RuntimeException {
    private ErrorDetails error;

    public BaseRuntimeException(ErrorDetails error) {
        super(error.message());
        this.error = error;
    }

    public ErrorDetails getError() {
        return error;
    }

    public Integer getCode() {
        return error.code();
    }
}
