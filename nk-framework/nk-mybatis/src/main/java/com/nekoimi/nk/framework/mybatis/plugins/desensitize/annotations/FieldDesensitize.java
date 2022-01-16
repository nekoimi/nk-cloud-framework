package com.nekoimi.nk.framework.mybatis.plugins.desensitize.annotations;

import com.nekoimi.nk.framework.mybatis.plugins.desensitize.enums.DesensitizeStrategy;

import java.lang.annotation.*;

/**
 * nekoimi  2022/1/16 23:04
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldDesensitize {

    /**
     * 脱敏策略
     * @return
     */
    DesensitizeStrategy value();
}
