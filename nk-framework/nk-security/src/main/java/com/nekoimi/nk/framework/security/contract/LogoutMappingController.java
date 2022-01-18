package com.nekoimi.nk.framework.security.contract;

import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;

/**
 * nekoimi  2022/1/18 13:27
 */
public interface LogoutMappingController {
    ServerWebExchangeMatcher mapping();
}
