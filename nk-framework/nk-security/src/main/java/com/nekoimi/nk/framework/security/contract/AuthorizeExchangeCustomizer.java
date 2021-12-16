package com.nekoimi.nk.framework.security.contract;

import org.springframework.security.config.web.server.ServerHttpSecurity;

/**
 * nekoimi  2021/12/16 21:00
 *
 * 授权配置定制器
 */
public interface AuthorizeExchangeCustomizer {

    /**
     * 授权定制
     * @param exchange
     */
    void customize(ServerHttpSecurity.AuthorizeExchangeSpec exchange);
}
