package com.nekoimi.nk.framework.core.contract;

import cn.hutool.core.convert.Convert;

/**
 * nekoimi  2022/2/8 9:50
 *
 * 自动类型转换接口
 */
public interface AutoConvert {

    /**
     * 默认类型转换实现
     * @param toType
     * @param <T>
     * @return
     */
    default <T> T convert(Class<T> toType) {
        return Convert.convert(toType, this);
    }
}
