package com.nekoimi.nk.framework.security.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/16 22:21
 *
 * request -> AuthenticationToken
 */
@Slf4j
@Component
public class ServerAllAuthenticationConverterManager implements ServerAuthenticationConverter {
    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return null;
    }
}
