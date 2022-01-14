package com.nekoimi.nk.framework.security.exception;

import com.nekoimi.nk.framework.core.error.BaseErrors;
import com.nekoimi.nk.framework.core.exception.BaseRuntimeException;

/**
 * nekoimi  2022/1/14 20:40
 */
public class RequestMissingAuthenticationParameterException extends BaseRuntimeException {
    public RequestMissingAuthenticationParameterException() {
        super(BaseErrors.HTTP_STATUS_BAD_REQUEST, "The request header is missing an authentication parameter");
    }
}
