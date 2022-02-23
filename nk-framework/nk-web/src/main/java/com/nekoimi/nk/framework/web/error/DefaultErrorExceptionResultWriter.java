package com.nekoimi.nk.framework.web.error;

import com.nekoimi.nk.framework.core.contract.error.ErrorDetails;
import com.nekoimi.nk.framework.core.protocol.JsonResp;
import com.nekoimi.nk.framework.core.utils.JsonUtils;
import com.nekoimi.nk.framework.web.contract.error.ErrorExceptionResultWriter;
import com.nekoimi.nk.framework.web.contract.error.ErrorExceptionWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * nekoimi  2021/12/13 22:23
 */
@Slf4j
@Component
@AllArgsConstructor
public class DefaultErrorExceptionResultWriter implements ErrorExceptionResultWriter {
    private final List<ErrorExceptionWriter> writers;

    private void doWriter(ServerWebExchange exchange, ErrorDetails error) {
        writers.forEach(writer -> {
            log.debug("result writer do writer -- {}", ClassUtils.getName(writer));
            writer.writer(exchange, error);
        });
    }

    public Mono<Void> httpWriter(ServerWebExchange exchange, ErrorDetails error) {
        return Mono.defer(() -> {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.OK);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

            JsonResp<Object> resp = JsonResp.error(error.code(), error.message());
            byte[] valueAsBytes = JsonUtils.writeBytes(resp);
            DataBuffer buffer = response.bufferFactory().wrap(valueAsBytes);
            return response.writeWith(Mono.just(buffer)).doOnError(e -> DataBufferUtils.release(buffer));
        });
    }

    @Override
    public Mono<Void> writer(ServerWebExchange exchange, ErrorDetails error) {
        doWriter(exchange, error);
        return httpWriter(exchange, error);
    }
}
