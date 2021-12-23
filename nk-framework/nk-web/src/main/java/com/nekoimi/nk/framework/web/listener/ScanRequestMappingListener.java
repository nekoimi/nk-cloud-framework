package com.nekoimi.nk.framework.web.listener;

import com.nekoimi.nk.framework.cache.contract.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.condition.PatternsRequestCondition;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.Map;
import java.util.Set;

/**
 * nekoimi  2021/12/21 21:09
 *
 * 获取全部api接口
 */
@Slf4j
public class ScanRequestMappingListener implements ApplicationListener<ApplicationReadyEvent> {
    private final CacheService cacheService;

    public ScanRequestMappingListener(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.debug("------- request mapping scan -------");
        ConfigurableApplicationContext context = event.getApplicationContext();
        ConfigurableEnvironment environment = context.getEnvironment();
        String microService = environment.getProperty("spring.application.name", "application");
        RequestMappingHandlerMapping handlerMapping = context.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo mappingInfo = entry.getKey();
            PatternsRequestCondition p = mappingInfo.getPatternsCondition();
            Set<PathPattern> patterns = p.getPatterns();
            HandlerMethod handlerMethod = entry.getValue();
        }
    }
}
