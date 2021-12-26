package com.nekoimi.nk.framework.security.handler;

import com.nekoimi.nk.framework.core.protocol.JsonResp;
import com.nekoimi.nk.framework.core.utils.JsonUtils;
import com.nekoimi.nk.framework.security.contract.AuthenticationTokenToResultTransformer;
import com.nekoimi.nk.framework.security.exception.ResultConverterSupportsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * nekoimi  2021/12/16 13:35
 * <p>
 * 登录成功
 */
@Slf4j
@Component
public class AuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {
    private final List<AuthenticationTokenToResultTransformer> converters;

    public AuthenticationSuccessHandler(List<AuthenticationTokenToResultTransformer> converters) {
        this.converters = converters;
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange exchange, Authentication authentication) {
        log.debug("登录成功 -- \n {}", JsonUtils.write(authentication));
        return Mono.just(exchange.getExchange().getResponse())
                .flatMap(response -> Flux.fromIterable(converters)
                        .filter(c -> c.support(authentication))
                        .switchIfEmpty(Flux.error(new ResultConverterSupportsException()))
                        .last()
                        .flatMap(c -> c.transform(authentication))
                        .map(JsonResp::ok)
                        .flatMap(result -> {
                            response.setStatusCode(HttpStatus.OK);
                            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                            DataBufferFactory dataBufferFactory = response.bufferFactory();
                            DataBuffer buffer = dataBufferFactory.wrap(JsonUtils.writeBytes(result));
                            return response.writeWith(Mono.just(buffer))
                                    .doOnError(error -> DataBufferUtils.release(buffer));
                        }));
    }
}
