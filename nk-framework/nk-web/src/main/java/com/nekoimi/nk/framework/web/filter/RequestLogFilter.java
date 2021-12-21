package com.nekoimi.nk.framework.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * nekoimi  2021/12/21 11:37
 * <p>
 * 请求/响应日志过滤器
 */
@Slf4j
public class RequestLogFilter implements WebFilter, Ordered {
    private static final String START_TIME = "startTime";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // 构建成一条长 日志，避免并发下日志错乱
        StringBuilder beforeReqLog = new StringBuilder(300);
        // 日志参数
        List<Object> beforeReqArgs = new ArrayList<>();
        beforeReqLog.append("\n\n================ Gateway Request Start  ================\n");
        // 打印路由
        beforeReqLog.append("===> {}: {}\n");
        // 参数
        String requestMethod = request.getMethodValue();
        String requestUrl = request.getURI().getRawPath();
        beforeReqArgs.add(requestMethod);
        beforeReqArgs.add(requestUrl);

        // 打印请求头
        HttpHeaders headers = request.getHeaders();
        headers.forEach((headerName, headerValue) -> {
            beforeReqLog.append("===Headers===  {}: {}\n");
            beforeReqArgs.add(headerName);
            beforeReqArgs.add(StringUtils.join(headerValue));
        });
        beforeReqLog.append("================ Gateway Request End =================\n");
        // 打印
        log.info(beforeReqLog.toString(), beforeReqArgs.toArray());

        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            Long startTime = exchange.getAttribute(START_TIME);
            long executeTime = 0L;
            if (startTime != null) {
                executeTime = (System.currentTimeMillis() - startTime);
            }

            // 构建成一条长 日志，避免并发下日志错乱
            StringBuilder responseLog = new StringBuilder(300);
            // 日志参数
            List<Object> responseArgs = new ArrayList<>();
            responseLog.append("\n\n================ Gateway Response Start  ================\n");
            // 打印路由 200 get: /mate*/xxx/xxx
            responseLog.append("<=== {} {}: {}: {}\n");
            // 参数
            responseArgs.add(Objects.requireNonNull(response.getStatusCode()).value());
            responseArgs.add(requestMethod);
            responseArgs.add(requestUrl);
            responseArgs.add(executeTime + "ms");

            // 打印请求头
            HttpHeaders httpHeaders = response.getHeaders();
            httpHeaders.forEach((headerName, headerValue) -> {
                responseLog.append("===Headers===  {}: {}\n");
                responseArgs.add(headerName);
                responseArgs.add(StringUtils.join(headerValue));
            });

            responseLog.append("================  Gateway Response End  =================\n");
            // 打印执行时间
            log.info(responseLog.toString(), responseArgs.toArray());
        }));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
