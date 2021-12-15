package com.nekoimi.nk.framework.base.exception;

import com.nekoimi.nk.framework.base.error.enums.BaseErrors;
import com.nekoimi.nk.framework.core.exception.NkRuntimeException;

/**
 * nekoimi  2021/12/14 11:11
 */
public class SystemClockErrorException extends NkRuntimeException {
    public SystemClockErrorException() {
        super(BaseErrors.SYSTEM_CLOCK_EXCEPTION);
    }
}
