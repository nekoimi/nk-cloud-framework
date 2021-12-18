package com.nekoimi.nk.framework.mybatis.exception;

import com.nekoimi.nk.framework.core.exception.BaseRuntimeException;
import com.nekoimi.nk.framework.mybatis.exception.enums.CrudErrors;

/**
 * nekoimi  2021/12/18 17:51
 */
public class FailedToResourceSaveException extends BaseRuntimeException {
    public FailedToResourceSaveException() {
        super(CrudErrors.RESOURCE_SAVING_FAILED);
    }

    public FailedToResourceSaveException(String message, Object... args) {
        super(CrudErrors.RESOURCE_SAVING_FAILED, message, args);
    }
}
