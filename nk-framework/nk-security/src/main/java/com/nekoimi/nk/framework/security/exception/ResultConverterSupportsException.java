package com.nekoimi.nk.framework.security.exception;

import com.nekoimi.nk.framework.core.error.BaseErrors;
import com.nekoimi.nk.framework.core.exception.BaseRuntimeException;

/**
 * nekoimi  2021/12/16 20:42
 */
public class ResultConverterSupportsException extends BaseRuntimeException {
    public ResultConverterSupportsException() {
        super(BaseErrors.RESULT_CONVERTER_SUPPORTS_EXCEPTION);
    }
}
