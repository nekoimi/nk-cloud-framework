package com.nekoimi.nk.framework.mybatis.exception;

import com.nekoimi.nk.framework.core.exception.BaseRuntimeException;
import com.nekoimi.nk.framework.mybatis.exception.enums.CrudErrors;

/**
 * nekoimi  2021/12/18 17:51
 */
public class FailedToResourceRemoveException extends BaseRuntimeException {
    public FailedToResourceRemoveException() {
        super(CrudErrors.RESOURCE_REMOVE_FAILED);
    }

    public FailedToResourceRemoveException(String message, Object... args) {
        super(CrudErrors.RESOURCE_REMOVE_FAILED, message, args);
    }
}
