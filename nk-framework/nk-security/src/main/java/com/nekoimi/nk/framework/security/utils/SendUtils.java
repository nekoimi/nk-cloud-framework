package com.nekoimi.nk.framework.security.utils;

import com.nekoimi.nk.framework.core.utils.JsonUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2022/1/12 23:13
 */
public class SendUtils {

    /**
     * send json response
     * @param response
     * @param body
     * @return
     */
    public static Mono<Void> sendJson(ServerHttpResponse response, Object body) {
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBufferFactory dataBufferFactory = response.bufferFactory();
        DataBuffer buffer = dataBufferFactory.wrap(JsonUtils.writeBytes(body));
        return response.writeWith(Mono.just(buffer))
                .doOnError(error -> DataBufferUtils.release(buffer));
    }
}
