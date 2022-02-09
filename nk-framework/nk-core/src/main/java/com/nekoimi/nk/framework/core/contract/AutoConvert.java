package com.nekoimi.nk.framework.core.contract;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;

import java.lang.reflect.Type;

/**
 * nekoimi  2022/2/8 9:50
 * <p>
 * 自动类型转换接口
 */
public interface AutoConvert {

    /**
     * @param toType
     * @param <T>
     * @return
     */
    default <T> T convert(Class<T> toType) {
        return Convert.convert(toType, this);
    }

    /**
     * @param reference
     * @param <T>
     * @return
     */
    default <T> T convert(TypeReference<T> reference) {
        return Convert.convert(reference, this);
    }

    /**
     * @param type
     * @param <T>
     * @return
     */
    default <T> T convert(Type type) {
        return Convert.convert(type, this);
    }
}
