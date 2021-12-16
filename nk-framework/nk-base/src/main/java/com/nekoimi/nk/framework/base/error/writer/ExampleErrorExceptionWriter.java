package com.nekoimi.nk.framework.base.error.writer;

import com.nekoimi.nk.framework.core.contract.error.ErrorDetails;
import com.nekoimi.nk.framework.base.contract.error.ErrorExceptionWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/14 9:57
 */
@Slf4j
@Component
public class ExampleErrorExceptionWriter implements ErrorExceptionWriter {
    @Override
    public Mono<Void> writer(ServerWebExchange exchange, ErrorDetails error) {
        log.warn("example error exception writer -- {}", error.message());
        return Mono.empty();
    }
}
