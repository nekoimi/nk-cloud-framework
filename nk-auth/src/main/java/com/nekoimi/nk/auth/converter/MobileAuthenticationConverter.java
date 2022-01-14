package com.nekoimi.nk.auth.converter;

import com.nekoimi.nk.framework.security.contract.AuthenticationType;
import com.nekoimi.nk.framework.security.contract.RequestToAuthenticationTokenConverter;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/26 22:35
 */
public class MobileAuthenticationConverter implements RequestToAuthenticationTokenConverter {
    @Override
    public boolean support(AuthenticationType authType) {
        return false;
    }

    @Override
    public Mono<? extends Authentication> convert(ServerWebExchange exchange) {
        return null;
    }
}
