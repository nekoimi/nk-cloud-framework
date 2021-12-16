package com.nekoimi.nk.framework.security.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/16 13:35
 * <p>
 * 登录成功
 */
@Slf4j
public class DefaultAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {
    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange filterExchange, Authentication authentication) {
        log.error("登录成功: {}", authentication);
        ObjectMapper objectMapper = new ObjectMapper();
        return Mono.defer(() -> Mono.just(filterExchange.getExchange().getResponse()))
                .flatMap(response -> {
                    response.setStatusCode(HttpStatus.OK);
                    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    DataBufferFactory dataBufferFactory = response.bufferFactory();
                    try {
                        byte[] bytes = objectMapper.writeValueAsBytes(authentication);
                        DataBuffer buffer = dataBufferFactory.wrap(bytes);
                        return response.writeWith(Mono.just(buffer))
                                .doOnError(error -> DataBufferUtils.release(buffer));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        return Mono.error(e);
                    }
                });
    }
}
