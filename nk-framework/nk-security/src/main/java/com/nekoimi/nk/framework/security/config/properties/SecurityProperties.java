package com.nekoimi.nk.framework.security.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * nekoimi  2021/12/16 21:07
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {
    private String loginUrl = "/auth/login";
    private String logoutUrl = "/auth/logout";
    private PathMatcher matcher = new PathMatcher();

    @Getter
    @Setter
    public static class PathMatcher {
        private List<String> permitAll = new ArrayList<>();
        private List<String> authenticated = new ArrayList<>();
    }
}