package com.nekoimi.nk.framework.security.filter;

import com.nekoimi.nk.framework.core.constant.SystemConstants;
import com.nekoimi.nk.framework.core.exception.http.RequestValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/17 10:02
 * <p>
 * 解析请求参数
 * 添加请求方式header
 */
@Slf4j
public class RequestParseAuthTypeFilter implements WebFilter {
    private static String authTypeParameter = "auth_type";
    private final ServerWebExchangeMatcher matcher;

    public RequestParseAuthTypeFilter(ServerWebExchangeMatcher matcher) {
        this.matcher = matcher;
    }

    private Mono<String> parse(ServerWebExchange exchange) {
        String authType = exchange.getRequest().getQueryParams().getFirst(authTypeParameter);
        if (StringUtils.isBlank(authType)) {
            return Mono.empty();
        }
        if (!StringUtils.isNumeric(authType)) {
            return Mono.empty();
        }
        log.debug("parse auth type: {}", authType);
        // 设置认证类型header
        exchange.getRequest().mutate().header(SystemConstants.AUTH_TYPE_REQUEST_HEADER, authType);
        return Mono.just(authType);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return Mono.just(matcher)
                .flatMap(m -> m.matches(exchange)
                        .filter(ServerWebExchangeMatcher.MatchResult::isMatch))
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
                .flatMap(r -> parse(exchange)
                        .switchIfEmpty(Mono.error(new RequestValidationException("Query params is missing `auth_type` parameter")))
                        .flatMap(s -> chain.filter(exchange)));
    }
}
