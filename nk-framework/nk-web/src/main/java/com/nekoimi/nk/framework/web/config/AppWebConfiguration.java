package com.nekoimi.nk.framework.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nekoimi.nk.framework.cache.contract.RedisService;
import com.nekoimi.nk.framework.core.holder.ObjectMapperHolder;
import com.nekoimi.nk.framework.web.controller.IndexController;
import com.nekoimi.nk.framework.web.customizer.HttpJackson2ObjectMapperBuilderCustomizer;
import com.nekoimi.nk.framework.web.filter.RequestLogFilter;
import com.nekoimi.nk.framework.web.handler.RewriteResponseBodyResultHandler;
import com.nekoimi.nk.framework.web.listener.CleanRequestMappingListener;
import com.nekoimi.nk.framework.web.listener.ScanRequestMappingListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import org.springframework.web.server.WebFilter;

/**
 * nekoimi  2021/12/22 10:30
 */
@Configuration
public class AppWebConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(30);
    }

    @Bean
    @ConditionalOnMissingBean(name = "indexController", search = SearchStrategy.CURRENT)
    public IndexController indexController() {
        return new IndexController();
    }

    @Bean
    @ConditionalOnProperty(name = "debug", havingValue = "true")
    public WebFilter requestLogFilter() {
        return new RequestLogFilter();
    }

    @Bean
    @ConditionalOnBean(value = RedisService.class, search = SearchStrategy.CURRENT)
    @ConditionalOnProperty(prefix = "app.web", name = "scan-request-mapping", havingValue = "true")
    public ScanRequestMappingListener scanRequestMappingListener(RedisService redisService) {
        return new ScanRequestMappingListener(redisService);
    }

    @Bean
    @ConditionalOnBean(value = RedisService.class, search = SearchStrategy.CURRENT)
    @ConditionalOnProperty(prefix = "app.web", name = "scan-request-mapping", havingValue = "true")
    public CleanRequestMappingListener cleanRequestMappingListener(RedisService redisService) {
        return new CleanRequestMappingListener(redisService);
    }

    @Bean
    @Primary
    @ConditionalOnClass(Jackson2ObjectMapperBuilder.class)
    public Jackson2ObjectMapperBuilderCustomizer builderCustomizer(ApplicationContext applicationContext,
                                                                   JacksonProperties jacksonProperties) {
        return new HttpJackson2ObjectMapperBuilderCustomizer(applicationContext, jacksonProperties);
    }

    /**
     * ??????????????????ObjectMapper??????
     *
     * @param builder
     * @return
     */
    @Bean
    @Primary
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        ObjectMapperHolder.setInstance(objectMapper);
        return objectMapper;
    }

    /**
     * ???????????????
     *
     * @param configurer
     * @param registry
     * @param resolver
     * @return
     *
     * @see org.springframework.web.reactive.config.WebFluxConfigurationSupport#responseBodyResultHandler(org.springframework.core.ReactiveAdapterRegistry, org.springframework.http.codec.ServerCodecConfigurer, org.springframework.web.reactive.accept.RequestedContentTypeResolver)
     */
    @Bean
    @Primary
    public ResponseBodyResultHandler rewriteResponseBodyResultHandler (ServerCodecConfigurer configurer,
                                                                       @Qualifier("webFluxAdapterRegistry") ReactiveAdapterRegistry registry,
                                                                       @Qualifier("webFluxContentTypeResolver") RequestedContentTypeResolver resolver) {
        RewriteResponseBodyResultHandler resultHandler = new RewriteResponseBodyResultHandler(configurer.getWriters(), resolver, registry);
        // resultHandler.setExcludes();
        return resultHandler;
    }
}
