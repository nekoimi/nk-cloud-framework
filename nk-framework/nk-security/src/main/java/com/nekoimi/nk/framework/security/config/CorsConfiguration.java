package com.nekoimi.nk.framework.security.config;

import com.nekoimi.nk.framework.security.config.properties.CorsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * nekoimi  2021/12/16 17:54
 */
@Configuration
public class CorsConfiguration {

    @Bean
    public org.springframework.web.cors.CorsConfiguration corsConfiguration(CorsProperties properties) {
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
        configuration.setAllowCredentials(properties.getAllowCredentials());
        configuration.setAllowedOrigins(properties.getAllowedOrigins());
        configuration.setAllowedMethods(properties.getAllowedMethods());
        configuration.setAllowedHeaders(properties.getAllowedHeaders());
        configuration.setExposedHeaders(properties.getExposedHeaders());
        configuration.setMaxAge(properties.getMaxAge());
        return configuration;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(org.springframework.web.cors.CorsConfiguration corsConfiguration) {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}