package com.nekoimi.nk.framework.security.contract;

import com.nekoimi.nk.framework.security.token.SubjectAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.Serializable;

/**
 * nekoimi  2021/12/17 15:22
 */
public interface ReactiveAuthenticationSupportProvider {
    /**
     * 判断是否支持该认证类型
     * @return
     */
    boolean support(Serializable authType);

    /**
     * 判断是否支持该AuthenticationToken的认证
     * @return
     */
    boolean support(Authentication authenticationToken);

    /**
     * 根据AuthenticationType将请求参数封装成对应的AuthenticationToken
     * @param exchange
     * @return
     */
    Mono<? extends Authentication> convert(ServerWebExchange exchange);

    /**
     * 根据AuthenticationToken完成认证
     * 返回认证结果
     * @param authentication
     * @return
     */
    Mono<SubjectAuthenticationToken> authenticate(Authentication authentication);
}
