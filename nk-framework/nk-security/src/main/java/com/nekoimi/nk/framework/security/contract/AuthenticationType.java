package com.nekoimi.nk.framework.security.contract;

/**
 * nekoimi  2022/1/14 21:05
 */
public interface AuthenticationType {

    /**
     * match
     * @param code
     * @return
     */
    boolean matches(Integer code);

    /**
     * match
     * @param authenticationType
     * @return
     */
    boolean matches(AuthenticationType authenticationType);
    /**
     * 类型编码
     * @return
     */
    Integer code();

    /**
     * 类型简介
     * @return
     */
    String description();
}
