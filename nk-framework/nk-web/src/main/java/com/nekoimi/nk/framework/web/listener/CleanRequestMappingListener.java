package com.nekoimi.nk.framework.web.listener;

import com.nekoimi.nk.framework.cache.contract.RedisService;
import com.nekoimi.nk.framework.core.constant.CacheKeyConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;

/**
 * nekoimi  2022/1/14 0:06
 */
@Slf4j
public class CleanRequestMappingListener implements DisposableBean {
    private final RedisService redisService;

    public CleanRequestMappingListener(RedisService redisService) {
        this.redisService = redisService;
    }

//    @Override
//    public void onApplicationEvent(ContextClosedEvent event) {
//        log.debug("------- clean request mapping -------");
//    }

    @Override
    public void destroy() throws Exception {
        log.debug("------- clean request mapping -------");
        redisService.delete(CacheKeyConstants.SCAN_REQUEST_MAPPING);
        redisService.delete(CacheKeyConstants.SCAN_REQUEST_MAPPING_BLACKLIST);
    }
}
