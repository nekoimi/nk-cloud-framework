package com.nekoimi.nk.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * nekoimi  2021/12/21 12:00
 */
@SpringBootApplication
@ComponentScan("com.nekoimi.nk")
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
