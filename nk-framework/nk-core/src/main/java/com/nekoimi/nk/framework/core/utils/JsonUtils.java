package com.nekoimi.nk.framework.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nekoimi.nk.framework.core.holder.ObjectMapperHolder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

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

    public static byte[] writeBytes(Object src) {
        try {
            return ObjectMapperHolder.getInstance().writeValueAsBytes(src);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return new byte[0];
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

    public static <T> T readBytes(byte[] json, Class<T> resultType) {
        try {
            return ObjectMapperHolder.getInstance().readValue(json, resultType);
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
