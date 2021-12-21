package com.nekoimi.nk.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * nekoimi  2021/12/21 12:46
 */
@SpringBootApplication
@ComponentScan("com.nekoimi.nk")
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
