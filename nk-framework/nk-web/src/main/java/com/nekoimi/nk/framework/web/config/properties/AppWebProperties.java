package com.nekoimi.nk.framework.web.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * nekoimi  2021/12/21 21:12
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.web")
public class AppWebProperties {
    // 接口永远返回200
    private Boolean responseForeverOk = false;
    // 是否扫描全部接口
    private Boolean scanRequestMapping;
    // swagger
    private Swagger swagger = new Swagger();

    @Getter
    @Setter
    public static class Swagger {
        private Boolean enabled = false;
        private List<String> permitAll = new ArrayList<>();
    }
}
