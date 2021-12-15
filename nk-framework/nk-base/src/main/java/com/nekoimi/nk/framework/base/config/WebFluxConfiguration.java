package com.nekoimi.nk.framework.base.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nekoimi.nk.framework.base.config.properties.CorsProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.CacheControl;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.util.concurrent.TimeUnit;

/**
 * nekoimi  2021/12/13 12:48
 */
@Slf4j
//@Configuration
@EnableWebFlux
@AllArgsConstructor
@Import(CorsProperties.class)
public class WebFluxConfiguration implements WebFluxConfigurer {
    private final CorsProperties properties;
    private final ObjectMapper objectMapper;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(properties.getPathPattern())
                .allowCredentials(properties.getAllowCredentials())
                .allowedOrigins(properties.getPathPattern())
                .allowedMethods(properties.getAllowedMethods().toArray(new String[0]))
                .allowedHeaders(properties.getAllowedHeaders().toArray(new String[0]))
                .exposedHeaders(properties.getExposedHeaders().toArray(new String[0]))
                .maxAge(properties.getMaxAge());
        log.debug("configure cors");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置knife4j显示文档
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                .resourceChain(false);

        log.debug("configure resource handlers");
    }
}
