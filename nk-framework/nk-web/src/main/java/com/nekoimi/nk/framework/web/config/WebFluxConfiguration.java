package com.nekoimi.nk.framework.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ResourceHandlerRegistration;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.WebFilter;

import java.time.Duration;

/**
 * nekoimi  2021/12/13 12:48
 *
 * @see WebFluxAutoConfiguration.WebFluxConfig
 */
@Slf4j
@Configuration
@EnableWebFlux
@AllArgsConstructor
public class WebFluxConfiguration implements WebFluxConfigurer {
    private final ObjectMapper objectMapper;
    private final ResourceProperties resourceProperties;

//    @Bean
//    @ConditionalOnProperty(name = "debug", havingValue = "true")
//    public WebFilter requestLogFilter() {
//
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!this.resourceProperties.isAddMappings()) {
            log.debug("Default resource handling disabled");
            return;
        }
        if (!registry.hasMappingForPattern("/webjars/**")) {
            ResourceHandlerRegistration registration = registry.addResourceHandler("/webjars/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/");
            configureResourceCaching(registration);
        }
        if (!registry.hasMappingForPattern("/**")) {
            ResourceHandlerRegistration registration = registry.addResourceHandler("/**")
                    .addResourceLocations(this.resourceProperties.getStaticLocations());
            configureResourceCaching(registration);
        }
    }

    private void configureResourceCaching(ResourceHandlerRegistration registration) {
        Duration cachePeriod = this.resourceProperties.getCache().getPeriod();
        ResourceProperties.Cache.Cachecontrol cacheControl = this.resourceProperties.getCache().getCachecontrol();
        if (cachePeriod != null && cacheControl.getMaxAge() == null) {
            cacheControl.setMaxAge(cachePeriod);
        }
        registration.setCacheControl(cacheControl.toHttpCacheControl());
    }

    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, MimeTypeUtils.APPLICATION_JSON));
        configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper, MimeTypeUtils.APPLICATION_JSON));
    }
}
