package com.nekoimi.nk.devtools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * nekoimi  2021/12/14 15:13
 */
@SpringBootApplication
@ComponentScan("com.nekoimi.nk")
public class GeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeneratorApplication.class, args);
    }
}
