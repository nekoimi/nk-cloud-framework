package com.nekoimi.nk.auth.provider;

import com.nekoimi.nk.auth.token.UsernamePasswordAuthenticationToken;
import com.nekoimi.nk.framework.core.exception.http.RequestValidationException;
import com.nekoimi.nk.framework.core.utils.JsonUtils;
import com.nekoimi.nk.framework.security.contract.ReactiveAuthenticationSupportProvider;
import com.nekoimi.nk.framework.security.token.SubjectAuthenticationToken;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.ByteArrayDecoder;
import org.springframework.core.codec.Hints;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.Map;

/**
 * nekoimi  2021/12/26 23:03
 */
@Component
public class UsernamePasswordReactiveAuthenticationSupportProvider implements ReactiveAuthenticationSupportProvider {
    private static final ResolvableType BYTES_TYPE = ResolvableType.forClass(byte[].class);
    private final ByteArrayDecoder decoder = new ByteArrayDecoder();
    private final String usernameParameter = "username";
    private final String passwordParameter = "password";

    @Override
    public boolean support(Serializable authType) {
        return "1".equals(authType);
    }

    public boolean support(Authentication authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication.getClass());
    }

    @Override
    public Mono<? extends Authentication> convert(ServerWebExchange exchange) {
        return decoder.decode(exchange.getRequest().getBody(), BYTES_TYPE, null, Hints.none())
                .flatMap(bytes -> Mono.justOrEmpty(JsonUtils.readBytes(bytes, Map.class)))
                .filter(map -> !map.isEmpty())
                .switchIfEmpty(Mono.error(new RequestValidationException()))
                .last()
                .flatMap(map -> {
                    String username = MapUtils.getString(map, usernameParameter);
                    if (StringUtils.isBlank(username)) {
                        return Mono.error(new RequestValidationException("Username can not be empty"));
                    }
                    String password = MapUtils.getString(map, passwordParameter);
                    if (StringUtils.isBlank(username)) {
                        return Mono.error(new RequestValidationException("Password can not be empty"));
                    }
                    return Mono.just(new UsernamePasswordAuthenticationToken(username, password));
                });
    }

    public Mono<SubjectAuthenticationToken> authenticate(Authentication authentication) {
        return Mono.just(authentication).cast(UsernamePasswordAuthenticationToken.class)
                .flatMap(token -> Mono.empty());
    }
}
