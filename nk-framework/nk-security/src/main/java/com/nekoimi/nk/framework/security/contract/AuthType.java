package com.nekoimi.nk.framework.security.contract;

import java.io.Serializable;

/**
 * nekoimi  2022/2/16 11:48
 */
public interface AuthType {
    boolean match(Serializable authType);
    Serializable value();
}
