package com.nekoimi.nk.framework.security.provider;

import com.nekoimi.nk.framework.core.exception.http.RequestValidationException;
import com.nekoimi.nk.framework.core.utils.JsonUtils;
import com.nekoimi.nk.framework.security.contract.ReactiveAuthenticationSupportProvider;
import com.nekoimi.nk.framework.security.token.SubjectAuthenticationToken;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.ByteArrayDecoder;
import org.springframework.core.codec.Hints;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.Map;

/**
 * nekoimi  2022/1/15 10:44
 */
public abstract class AbstractReactiveAuthenticationSupportProvider implements ReactiveAuthenticationSupportProvider {
    private static final ResolvableType BYTES_TYPE = ResolvableType.forClass(byte[].class);
    private final ByteArrayDecoder decoder = new ByteArrayDecoder();

    /**
     * 认证类型标识
     *
     * @return
     */
    abstract protected Serializable authType();

    /**
     * AuthenticationToken clazz
     *
     * @return
     */
    abstract protected Class<? extends Authentication> authenticationTokenClazz();

    /**
     * do convert
     *
     * @param requestParameters 请求参数
     * @return
     */
    abstract protected Mono<? extends Authentication> doConvert(Map<String, Object> requestParameters);

    /**
     * do authentication
     * @param authentication
     * @return
     */
    abstract protected Mono<SubjectAuthenticationToken> doAuthenticate(Authentication authentication);

    @Override
    public boolean support(Serializable authType) {
        if (authType == null || authType() == null)
            return false;
        return authType.equals(authType());
    }

    @Override
    public boolean support(Authentication authenticationToken) {
        if (authenticationToken == null || authenticationTokenClazz() == null)
            return false;
        return authenticationToken.getClass() == authenticationTokenClazz();
    }

    @Override
    public Mono<? extends Authentication> convert(ServerWebExchange exchange) {
        return decoder.decode(exchange.getRequest().getBody(), BYTES_TYPE, null, Hints.none())
                .flatMap(bytes -> Mono.justOrEmpty(JsonUtils.readBytes(bytes, Map.class)))
                .filter(map -> !map.isEmpty())
                .switchIfEmpty(Mono.error(new RequestValidationException()))
                .last()
                .flatMap(this::doConvert);
    }

    @Override
    public Mono<SubjectAuthenticationToken> authenticate(Authentication authentication) {
        return doAuthenticate(authentication);
    }
}
