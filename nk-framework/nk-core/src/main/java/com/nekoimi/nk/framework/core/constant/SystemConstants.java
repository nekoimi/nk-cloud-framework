package com.nekoimi.nk.framework.core.constant;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * nekoimi  2021/12/6 11:44
 */
public interface SystemConstants {
    /**
     * 默认时间格式
     */
    String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
    DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
    TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai"));

    /**
     * 默认请求方式header头
     */
    String AUTH_TYPE_REQUEST_HEADER = "X-Auth-Type";
}
