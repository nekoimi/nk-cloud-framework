package com.nekoimi.nk.framework.core.exception;

import com.nekoimi.nk.framework.core.error.ErrorContract;

/**
 * nekoimi  2021/12/6 14:37
 */
public class NkRuntimeException extends RuntimeException {
    private ErrorContract error;

    public NkRuntimeException(ErrorContract error) {
        super(error.message());
        this.error = error;
    }

    public ErrorContract getError() {
        return error;
    }

    public Integer getCode() {
        return error.code();
    }
}
