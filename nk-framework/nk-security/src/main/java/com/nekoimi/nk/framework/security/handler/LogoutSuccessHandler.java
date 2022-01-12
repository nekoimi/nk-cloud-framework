package com.nekoimi.nk.framework.security.handler;

import com.nekoimi.nk.framework.core.protocol.JsonResp;
import com.nekoimi.nk.framework.security.utils.SendUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/16 16:18
 */
@Slf4j
@Component
public class LogoutSuccessHandler implements ServerLogoutSuccessHandler {
    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
        log.debug("------- logout success -------");
        return Mono.just(exchange.getExchange().getResponse()).flatMap(response -> SendUtils.sendJson(response, JsonResp.ok()));
    }
}
