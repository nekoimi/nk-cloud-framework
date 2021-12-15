package com.nekoimi.nk.framework.base.error.enums;

import com.nekoimi.nk.framework.core.error.ErrorContract;

/**
 * nekoimi  2021/12/10 17:12
 */
public enum BaseErrors implements ErrorContract {
    HTTP_STATUS_BAD_REQUEST(10400, "Bad request"),
    HTTP_STATUS_UNAUTHORIZED(10401, "Unauthorized"),
    HTTP_STATUS_FORBIDDEN(10403, "Forbidden"),
    HTTP_STATUS_NOT_FOUND(10404, "Not found"),
    HTTP_STATUS_METHOD_NOT_ALLOWED(10405, "Method not allowed"),

    SYSTEM_CLOCK_EXCEPTION(50000, "System clock error"),
    // 客户端请求异常
    DEFAULT_CLIENT_ERROR(99400, "无效的请求！"),
    // 未捕获的异常，系统发生致命错误，提示系统维护更新!
    DEFAULT_SERVER_ERROR(99500, "系统更新中！请稍候再试～");

    private Integer code;
    private String message;

    BaseErrors(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public String trace() {
        return null;
    }

    @Override
    public String exception() {
        return null;
    }
}
