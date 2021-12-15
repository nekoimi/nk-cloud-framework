package com.nekoimi.nk.framework.core.error;

/**
 * nekoimi  2021/12/6 14:43
 *
 * 通用异常接口
 */
public interface ErrorContract {
    Integer code();
    String message();
    String trace();
    String exception();
}
