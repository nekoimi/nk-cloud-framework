package com.nekoimi.nk.framework.security.converter;

import com.nekoimi.nk.framework.security.enums.AuthType;
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
    private final AuthType authType;
    private final ServerAuthenticationConverter converter;

    public AuthenticationConverterAdapter(AuthType authType,
                                          ServerAuthenticationConverter converter) {
        this.authType = authType;
        this.converter = converter;
    }

    @Override
    public boolean support(AuthType authType) {
        return this.authType == authType;
    }

    @Override
    public Mono<? extends Authentication> convert(ServerWebExchange exchange) {
        return this.converter.convert(exchange);
    }
}
