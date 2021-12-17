package com.nekoimi.nk.framework.security.contract;

import com.nekoimi.nk.framework.security.constant.AuthType;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/17 9:29
 *
 * 根据AuthType将请求参数封装成对应的AuthenticationToken
 * request -> AuthenticationToken
 */
public interface RequestToAuthenticationTokenConverter {
    /**
     * 支持的请求方式
     * @return
     */
    boolean support(AuthType authType);

    /**
     * 封装
     * @param exchange
     * @return
     */
    Mono<Authentication> convert(ServerWebExchange exchange);
}
