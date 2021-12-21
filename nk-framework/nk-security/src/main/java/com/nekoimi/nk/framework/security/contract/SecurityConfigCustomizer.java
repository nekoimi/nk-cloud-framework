package com.nekoimi.nk.framework.security.contract;

import org.springframework.security.config.web.server.ServerHttpSecurity;

/**
 * nekoimi  2021/12/21 23:39
 * <p>
 * Spring Security 自定义配置器
 * 其他项目可直接自定义实现扩展默认的配置
 */
public interface SecurityConfigCustomizer {

    /**
     * @param http
     */
    void customize(ServerHttpSecurity http);
}
