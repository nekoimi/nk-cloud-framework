package com.nekoimi.nk.framework.web.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * nekoimi  2021/12/21 21:09
 *
 * 获取全部api接口
 */
@Slf4j
public class ScanRequestMappingListener implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.debug("------- request mapping scan -------");
    }
}
