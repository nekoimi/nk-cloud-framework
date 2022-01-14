package com.nekoimi.nk.framework.security.adapter;

import com.nekoimi.nk.framework.security.contract.AuthenticationType;
import com.nekoimi.nk.framework.security.contract.RequestToAuthenticationTokenConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/17 13:07
 *
 * spring security 默认验证器包装类
 */
public class AuthenticationConverterAdapter implements RequestToAuthenticationTokenConverter {
    private final AuthenticationType authType;
    private final ServerAuthenticationConverter converter;

    public AuthenticationConverterAdapter(AuthenticationType authType, ServerAuthenticationConverter converter) {
        this.authType = authType;
        this.converter = converter;
    }

    @Override
    public boolean support(AuthenticationType authType) {
        return this.authType.matches(authType);
    }

    @Override
    public Mono<? extends Authentication> convert(ServerWebExchange exchange) {
        return this.converter.convert(exchange);
    }
}
