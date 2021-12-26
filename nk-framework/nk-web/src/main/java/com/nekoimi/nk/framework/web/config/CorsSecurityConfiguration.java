package com.nekoimi.nk.framework.web.config;

import com.nekoimi.nk.framework.web.config.properties.CorsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.WebFilter;

/**
 * nekoimi  2021/12/16 17:54
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "app.cors", name = "enabled", havingValue = "true")
public class CorsSecurityConfiguration {

    @Bean
    public CorsConfiguration corsConfiguration(CorsProperties properties) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(properties.getAllowCredentials());
        configuration.setAllowedOrigins(properties.getAllowedOrigins());
        configuration.setAllowedMethods(properties.getAllowedMethods());
        configuration.setAllowedHeaders(properties.getAllowedHeaders());
        configuration.setExposedHeaders(properties.getExposedHeaders());
        configuration.setMaxAge(properties.getMaxAge());
        return configuration;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(CorsConfiguration corsConfiguration) {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public WebFilter corsWebFilter(CorsConfigurationSource source) {
        log.debug("cors filter ...");
        return new CorsWebFilter(source);
    }
}
