package com.nekoimi.nk.framework.mybatis.exception;

import com.nekoimi.nk.framework.core.exception.BaseRuntimeException;
import com.nekoimi.nk.framework.mybatis.exception.enums.CrudErrors;

/**
 * nekoimi  2021/12/18 21:12
 */
public class FailedToResourceQueryException extends BaseRuntimeException {
    public FailedToResourceQueryException() {
        super(CrudErrors.RESOURCE_QUERY_FAILED);
    }

    public FailedToResourceQueryException(String message, Object... args) {
        super(CrudErrors.RESOURCE_QUERY_FAILED, message, args);
    }
}
