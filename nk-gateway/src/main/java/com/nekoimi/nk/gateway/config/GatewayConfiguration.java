package com.nekoimi.nk.gateway.config;

import com.nekoimi.nk.framework.web.config.properties.AppWebProperties;
import com.nekoimi.nk.gateway.filter.RewriteUpstreamResponseBodyGatewayFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.config.conditional.ConditionalOnEnabledGlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * nekoimi  2021/12/21 16:58
 */
@Slf4j
@Configuration
public class GatewayConfiguration {

    @Bean
    @ConditionalOnEnabledGlobalFilter
    public RewriteUpstreamResponseBodyGatewayFilter rewriteUpstreamResponseBodyGatewayFilter(AppWebProperties appWebProperties,
                                                                                             ModifyResponseBodyGatewayFilterFactory filterFactory) {
        return new RewriteUpstreamResponseBodyGatewayFilter(appWebProperties, filterFactory);
    }
}
