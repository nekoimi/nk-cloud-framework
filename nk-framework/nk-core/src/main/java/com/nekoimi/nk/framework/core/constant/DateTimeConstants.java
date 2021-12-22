package com.nekoimi.nk.framework.core.constant;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * nekoimi  2021/12/6 11:44
 *
 * 时间相关
 */
public interface DateTimeConstants {
    String DEFAULT_ZONE = "Asia/Shanghai";
    String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
    DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
    TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone(ZoneId.of(DEFAULT_ZONE));
}
