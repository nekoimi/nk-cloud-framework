package com.nekoimi.nk.framework.core.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * nekoimi  2021/12/14 11:21
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.id-gen")
public class IdGenProperties {
    // 机器ID
    private long workerId;
    // 数据中心ID
    private long dataCenterId;
}
