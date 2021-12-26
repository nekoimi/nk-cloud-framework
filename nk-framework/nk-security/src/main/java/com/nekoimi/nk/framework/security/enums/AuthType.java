package com.nekoimi.nk.framework.security.enums;

import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/17 9:33
 *
 * 综合验证方式类型
 */
public enum AuthType {
    USERNAME_PASSWORD(0),
    MOBILE(1)
    ;

    private final int id;

    AuthType() {
        this.id = ordinal() + 1;
    }

    AuthType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Mono<AuthType> valueOf(Integer id) {
        if (id == null) return Mono.empty();
        for (AuthType type : values()) {
            if (type.id == id) {
                return Mono.just(type);
            }
        }
        return Mono.empty();
    }

    @Override
    public String toString() {
        return "" + id;
    }
}
