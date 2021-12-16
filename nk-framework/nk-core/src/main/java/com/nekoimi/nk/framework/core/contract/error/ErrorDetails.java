package com.nekoimi.nk.framework.core.contract.error;

/**
 * nekoimi  2021/12/6 14:43
 *
 * 通用异常接口
 */
public interface ErrorDetails {
    Integer code();
    String message();
    String trace();
    String exception();
}
