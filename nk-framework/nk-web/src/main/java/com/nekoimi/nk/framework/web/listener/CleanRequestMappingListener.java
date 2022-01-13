package com.nekoimi.nk.framework.web.listener;

import com.nekoimi.nk.framework.cache.contract.CacheService;
import com.nekoimi.nk.framework.core.constant.CacheKeyConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

/**
 * nekoimi  2022/1/14 0:06
 */
@Slf4j
public class CleanRequestMappingListener implements DisposableBean {
    private final CacheService cacheService;

    public CleanRequestMappingListener(CacheService cacheService) {
        this.cacheService = cacheService;
    }

//    @Override
//    public void onApplicationEvent(ContextClosedEvent event) {
//        log.debug("------- clean request mapping -------");
//    }

    @Override
    public void destroy() throws Exception {
        log.debug("------- clean request mapping -------");
        cacheService.delete(CacheKeyConstants.SCAN_REQUEST_MAPPING);
        cacheService.delete(CacheKeyConstants.SCAN_REQUEST_MAPPING_BLACKLIST);
    }
}
