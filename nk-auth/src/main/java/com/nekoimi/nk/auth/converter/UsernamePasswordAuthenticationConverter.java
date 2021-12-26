package com.nekoimi.nk.auth.converter;

import com.nekoimi.nk.auth.token.UsernamePasswordAuthenticationToken;
import com.nekoimi.nk.framework.core.exception.http.RequestValidationException;
import com.nekoimi.nk.framework.core.utils.JsonUtils;
import com.nekoimi.nk.framework.security.contract.RequestToAuthenticationTokenConverter;
import com.nekoimi.nk.framework.security.enums.AuthType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.ByteArrayDecoder;
import org.springframework.core.codec.Hints;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * nekoimi  2021/12/26 21:49
 */
@Component
public class UsernamePasswordAuthenticationConverter implements RequestToAuthenticationTokenConverter {
    private static final ResolvableType BYTES_TYPE = ResolvableType.forClass(byte[].class);
    private final ByteArrayDecoder decoder = new ByteArrayDecoder();
    private final String usernameParameter = "username";
    private final String passwordParameter = "password";

    @Override
    public boolean support(AuthType authType) {
        return AuthType.USERNAME_PASSWORD == authType;
    }

    @Override
    public Mono<? extends Authentication> convert(ServerWebExchange exchange) {
        return decoder.decode(exchange.getRequest().getBody(), BYTES_TYPE, null, Hints.none())
                .flatMap(bytes -> Mono.justOrEmpty(JsonUtils.readBytes(bytes, Map.class)))
                .filter(map -> !map.isEmpty())
                .last()
                .flatMap(map -> {
                    String username = String.valueOf(map.get(usernameParameter));
                    if (StringUtils.isBlank(username)) {
                        return Mono.error(new RequestValidationException("Username can not be empty"));
                    }
                    String password = String.valueOf(map.get(passwordParameter));
                    if (StringUtils.isBlank(username)) {
                        return Mono.error(new RequestValidationException("Password can not be empty"));
                    }
                    return Mono.just(new UsernamePasswordAuthenticationToken(username, password));
                });
    }
}
