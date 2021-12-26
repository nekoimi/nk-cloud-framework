package com.nekoimi.nk.gateway.filter;

import com.nekoimi.nk.framework.core.protocol.JsonResp;
import com.nekoimi.nk.framework.core.utils.JsonUtils;
import com.nekoimi.nk.framework.web.config.properties.WebProperties;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * nekoimi  2021/12/26 16:13
 * <p>
 * 包装其他服务的返回值统一返回
 */
public class RewriteUpstreamResponseBodyGatewayFilter implements GlobalFilter, Ordered {
    private final ModifyResponseBodyGatewayFilterFactory filterFactory;
    private final WebProperties webProperties;
    private GatewayFilter filter;

    public RewriteUpstreamResponseBodyGatewayFilter(WebProperties webProperties,
                                                    ModifyResponseBodyGatewayFilterFactory filterFactory) {
        this.webProperties = webProperties;
        this.filterFactory = filterFactory;
        initialization();
    }

    private void initialization() {
        RewriteFunction<byte[], byte[]> rewriteFunction = new RewriteUpstreamResponseBodyFunction(webProperties);
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
        private final WebProperties webProperties;
        public RewriteUpstreamResponseBodyFunction(WebProperties webProperties) {
            this.webProperties = webProperties;
        }

        @Override
        public Publisher<byte[]> apply(ServerWebExchange exchange, byte[] bytes) {
            if (!ServerWebExchangeUtils.isAlreadyRouted(exchange) ||
                    exchange.getResponse().getStatusCode() == null ||
                    exchange.getResponse().getHeaders().getContentType() == null ||
                    !exchange.getResponse().getStatusCode().is2xxSuccessful() ||
                    !exchange.getResponse().getHeaders().getContentType().isCompatibleWith(MediaType.APPLICATION_JSON)) {
                if (webProperties.getResponseForeverOk()) {
                    exchange.getResponse().setStatusCode(HttpStatus.OK);
                }
                return Mono.justOrEmpty(bytes);
            }
            log.debug("rewrite response ...");
            return Mono.justOrEmpty(JsonUtils.readBytes(bytes, Map.class)).map(JsonResp::ok).map(JsonUtils::writeBytes);
        }
    }

}
