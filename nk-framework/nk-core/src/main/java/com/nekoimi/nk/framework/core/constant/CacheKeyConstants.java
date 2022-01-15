package com.nekoimi.nk.framework.core.constant;

/**
 * nekoimi  2021/12/22 9:30
 * <p>
 * 缓存键
 */
public interface CacheKeyConstants {
    /**
     * 扫描路由缓存
     */
    String SCAN_REQUEST_MAPPING = "scan_request_mapping:all";
    /**
     * 扫描路由黑名单缓存
     */
    String SCAN_REQUEST_MAPPING_BLACKLIST = "scan_request_mapping:blacklist";
    /**
     * 认证信息缓存
     */
    String AUTHENTICATION_SUBJECT = "authentication:sub";
}
