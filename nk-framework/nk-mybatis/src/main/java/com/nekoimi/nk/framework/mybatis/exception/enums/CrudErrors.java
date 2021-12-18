package com.nekoimi.nk.framework.mybatis.exception.enums;

import com.nekoimi.nk.framework.core.contract.error.ErrorDetails;

/**
 * nekoimi  2021/12/18 17:53
 */
public enum CrudErrors implements ErrorDetails {
    RESOURCE_NOT_FOUND(10600, "resource not found"),
    RESOURCE_QUERY_FAILED(10601, "resource query found"),
    RESOURCE_SAVING_FAILED(10602, "resource saving failed"),
    RESOURCE_UPDATE_FAILED(10603, "resource update failed"),
    RESOURCE_REMOVE_FAILED(10604, "resource remove failed")

            ;

    private Integer code;
    private String message;

    CrudErrors(Integer code, String message) {
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
}