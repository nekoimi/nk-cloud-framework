package com.nekoimi.nk.platform.admin.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * nekoimi  2021/12/26 15:03
 */
@SpringBootApplication
@ComponentScan("com.nekoimi.nk")
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}
