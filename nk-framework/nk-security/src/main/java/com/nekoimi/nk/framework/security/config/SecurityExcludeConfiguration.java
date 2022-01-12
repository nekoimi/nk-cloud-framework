package com.nekoimi.nk.framework.security.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * nekoimi  2021/12/24 15:22
 *
 * 排除掉SecurityAutoConfiguration自动配置
 * 默认不启用SpringSecurity
 */
@Configuration
@EnableAutoConfiguration(exclude = ReactiveSecurityAutoConfiguration.class)
public class SecurityExcludeConfiguration {
}
