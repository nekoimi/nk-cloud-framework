package com.nekoimi.nk.auth.enums;

import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/17 9:33
 *
 * 综合验证方式类型
 */
public enum AuthType {
    USERNAME_PASSWORD(1),
    MOBILE(2)
    ;

    private final Integer code;

    AuthType() {
        this.code = ordinal() + 1;
    }

    AuthType(int code) {
        this.code = code;
    }

    public Integer code() {
        return code;
    }

    public static Mono<AuthType> valueOf(Integer code) {
        if (code == null) return Mono.empty();
        for (AuthType type : values()) {
            if (type.code.equals(code)) {
                return Mono.just(type);
            }
        }
        return Mono.empty();
    }

    @Override
    public String toString() {
        return "" + code;
    }
}
