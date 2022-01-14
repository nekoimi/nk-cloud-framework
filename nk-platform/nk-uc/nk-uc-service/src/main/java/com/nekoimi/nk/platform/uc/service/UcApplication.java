package com.nekoimi.nk.platform.uc.service;

import com.nekoimi.nk.framework.security.annotations.EnableSecurityAccess;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * nekoimi  2021/12/23 16:19
 */
@SpringBootApplication
@ComponentScan("com.nekoimi.nk")
@EnableSecurityAccess
public class UcApplication {

    public static void main(String[] args) {
        SpringApplication.run(UcApplication.class, args);
    }
}
