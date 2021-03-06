package com.nekoimi.nk.auth;

import com.nekoimi.nk.framework.security.annotations.EnableSecurityAccessAuthServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * nekoimi  2021/12/15 21:48
 */
@SpringBootApplication
@ComponentScan("com.nekoimi.nk")
@EnableSecurityAccessAuthServer
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
