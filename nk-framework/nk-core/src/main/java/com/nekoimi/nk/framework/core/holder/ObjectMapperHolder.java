package com.nekoimi.nk.framework.core.holder;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * nekoimi  2021/12/14 10:53
 */
public class ObjectMapperHolder {
    private static ObjectMapper INSTANCE = new ObjectMapper();

    public static ObjectMapper getInstance() {
        return INSTANCE;
    }

    public static void setInstance(ObjectMapper INSTANCE) {
        ObjectMapperHolder.INSTANCE = INSTANCE;
    }
}
