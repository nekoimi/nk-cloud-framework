package com.nekoimi.nk.framework.web.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * nekoimi  2021/12/21 21:12
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.web")
public class WebProperties {
    private Boolean scanRequestMapping;
}
