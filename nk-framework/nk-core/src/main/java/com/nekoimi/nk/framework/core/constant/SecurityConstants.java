package com.nekoimi.nk.framework.core.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * nekoimi  2021/12/22 11:02
 */
public final class SecurityConstants {
    public static final String LOGIN_PATH = "/auth/login";
    public static final String LOGOUT_PATH = "/auth/logout";
    private static final List<String> DEFAULT_PERMIT_ALL = new ArrayList<>();

    static {
        DEFAULT_PERMIT_ALL.add("/");
        DEFAULT_PERMIT_ALL.add("/doc.html");
        DEFAULT_PERMIT_ALL.add("/v2/api-docs");
        DEFAULT_PERMIT_ALL.add("/webjars/**");
        DEFAULT_PERMIT_ALL.add("/swagger-resources");
        DEFAULT_PERMIT_ALL.add("/swagger-resources/**");
        DEFAULT_PERMIT_ALL.add(LOGIN_PATH);
        DEFAULT_PERMIT_ALL.add(LOGOUT_PATH);
    }

    public static List<String> getDefaultPermitAll() {
        return DEFAULT_PERMIT_ALL;
    }
}
