package com.nekoimi.nk.framework.core.exception;


import com.nekoimi.nk.framework.core.error.BaseErrors;

/**
 * nekoimi  2021/12/14 11:11
 */
public class SystemClockErrorException extends BaseRuntimeException {
    public SystemClockErrorException() {
        super(BaseErrors.SYSTEM_CLOCK_EXCEPTION);
    }
}
