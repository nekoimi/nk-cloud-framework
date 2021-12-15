package com.nekoimi.nk.framework.base.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nekoimi.nk.framework.base.holder.ObjectMapperHolder;
import lombok.extern.slf4j.Slf4j;

/**
 * nekoimi  2021/12/14 10:55
 */
@Slf4j
public class JsonUtils {

    public static String write(Object src) {
        try {
            return ObjectMapperHolder.getInstance().writeValueAsString(src);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    public static <T> T read(String json, Class<T> resultType) {
        try {
            return ObjectMapperHolder.getInstance().readValue(json, resultType);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}