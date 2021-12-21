package com.nekoimi.nk.framework.security.contract;

import org.springframework.security.config.web.server.ServerHttpSecurity;

/**
 * nekoimi  2021/12/21 23:45
 * <p>
 * 自定义认证过滤规则处理器 可有多个实现
 */
public interface SecurityAuthorizeExchangeCustomizer {

    /**
     * @param exchange
     */
    void customize(ServerHttpSecurity.AuthorizeExchangeSpec exchange);
}
