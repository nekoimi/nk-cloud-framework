package com.nekoimi.nk.framework.mybatis.plugins.desensitize;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;

/**
 * nekoimi  2022/1/16 20:38
 */
public class DesensitizeConfigurationCustomizer implements ConfigurationCustomizer {
    @Override
    public void customize(MybatisConfiguration configuration) {
        configuration.addInterceptor(new DesensitizeResultInterceptor());
    }
}
