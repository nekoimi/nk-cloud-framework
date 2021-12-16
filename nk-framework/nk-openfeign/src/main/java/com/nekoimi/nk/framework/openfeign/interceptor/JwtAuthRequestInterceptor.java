package com.nekoimi.nk.framework.openfeign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * nekoimi  2021/12/16 10:10
 */
@Slf4j
@Component
public class JwtAuthRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        log.debug(requestTemplate.bodyTemplate());
    }
}
