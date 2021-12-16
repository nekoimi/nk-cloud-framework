package com.nekoimi.nk.framework.openfeign.cofnig;

import feign.Logger;
import feign.hystrix.FallbackFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * nekoimi  2021/12/16 9:58
 */
@Configuration
@FeignClient
@EnableFeignClients
public class OpenFeignConfiguration {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
