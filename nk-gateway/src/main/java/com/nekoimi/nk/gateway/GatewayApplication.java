package com.nekoimi.nk.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.filter.CorsFilter;

/**
 * nekoimi  2021/12/7 10:44
 */
@SpringBootApplication
@ComponentScan(basePackages = {
        "com.nekoimi.nk.framework",
        "com.nekoimi.nk.gateway"
})
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
