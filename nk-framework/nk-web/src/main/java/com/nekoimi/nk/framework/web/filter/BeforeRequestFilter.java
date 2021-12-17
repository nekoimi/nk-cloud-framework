package com.nekoimi.nk.framework.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/13 12:41
 */
@Slf4j
@Component
public class BeforeRequestFilter implements WebFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if ("/favicon.ico".equalsIgnoreCase(request.getPath().value())) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.NO_CONTENT);
            DataBuffer dataBuffer = response.bufferFactory().allocateBuffer();
            return response.writeWith(Mono.just(dataBuffer))
                    .doOnError(e -> DataBufferUtils.release(dataBuffer));
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
