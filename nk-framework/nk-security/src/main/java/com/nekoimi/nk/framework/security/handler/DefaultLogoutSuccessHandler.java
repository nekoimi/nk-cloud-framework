package com.nekoimi.nk.framework.security.handler;

import com.nekoimi.nk.framework.core.protocol.JsonResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/16 16:18
 */
@Slf4j
public class DefaultLogoutSuccessHandler implements ServerLogoutSuccessHandler {
    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
        log.debug("------- logout success -------");
        return Mono.fromRunnable(() -> exchange.getExchange()
                .getResponse()
                .setStatusCode(HttpStatus.OK));
    }
}
