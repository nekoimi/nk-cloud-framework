package com.nekoimi.nk.gateway.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.nekoimi.nk.framework.core.protocol.JsonResp;
import com.nekoimi.nk.framework.core.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/26 16:13
 * <p>
 * 包装其他服务的返回值统一返回
 */
public class RewriteUpstreamResponseBodyGatewayFilter implements GlobalFilter, Ordered {
    private final ModifyResponseBodyGatewayFilterFactory filterFactory;
    private GatewayFilter filter;

    public RewriteUpstreamResponseBodyGatewayFilter(ModifyResponseBodyGatewayFilterFactory filterFactory) {
        this.filterFactory = filterFactory;
        initialization();
    }

    private void initialization() {
        RewriteFunction<byte[], byte[]> rewriteFunction = new RewriteUpstreamResponseBodyFunction();
        ModifyResponseBodyGatewayFilterFactory.Config modifyConfig =
                new ModifyResponseBodyGatewayFilterFactory.Config()
                        .setRewriteFunction(byte[].class, byte[].class, rewriteFunction);
        filter = filterFactory.apply(modifyConfig);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return filter.filter(exchange, chain);
    }

    @Override
    public int getOrder() {
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;
    }


    /**
     * 响应重写函数
     */
    @Slf4j
    public static class RewriteUpstreamResponseBodyFunction implements RewriteFunction<byte[], byte[]> {
        @Override
        public Publisher<byte[]> apply(ServerWebExchange exchange, byte[] bytes) {
            boolean isAnythingTodo = !ServerWebExchangeUtils.isAlreadyRouted(exchange) ||
                    exchange.getResponse().getStatusCode() == null ||
                    exchange.getResponse().getHeaders().getContentType() == null ||
                    !exchange.getResponse().getStatusCode().is2xxSuccessful() ||
                    !exchange.getResponse().getHeaders().getContentType().isCompatibleWith(MediaType.APPLICATION_JSON);

            if (isAnythingTodo) {
                return Mono.justOrEmpty(bytes);
            }

            log.debug("rewrite response");
            JsonResp<?> resp = JsonUtils.readBytes(bytes, new TypeReference<JsonResp<?>>() {});
            if (resp == null) {
                log.error("read response null");
                return Mono.justOrEmpty(bytes);
            }

            if (resp.isOk()) {
                return Mono.justOrEmpty(bytes);
            }

            // TODO
            // todo something, write error log...
            return Mono.justOrEmpty(bytes);
        }
    }

}
