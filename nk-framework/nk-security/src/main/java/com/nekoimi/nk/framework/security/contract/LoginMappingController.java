package com.nekoimi.nk.framework.security.contract;

import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;

/**
 * nekoimi  2022/1/18 13:26
 */
public interface LoginMappingController {
    ServerWebExchangeMatcher mapping();
}
