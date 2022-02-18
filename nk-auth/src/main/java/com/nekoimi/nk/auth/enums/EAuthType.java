package com.nekoimi.nk.auth.enums;

import com.nekoimi.nk.framework.security.contract.AuthType;

import java.io.Serializable;

/**
 * nekoimi  2021/12/17 9:33
 *
 * 综合验证方式类型
 */
public enum EAuthType implements AuthType {
    USERNAME_PASSWORD(1),
    MOBILE(2)
    ;

    private final int code;

    EAuthType(int code) {
        this.code = code;
    }

    @Override
    public boolean match(Serializable authType) {
        return String.valueOf(code).equals(authType);
    }

    @Override
    public Serializable value() {
        return code;
    }
}
