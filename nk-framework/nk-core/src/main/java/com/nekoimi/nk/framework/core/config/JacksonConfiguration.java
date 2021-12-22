package com.nekoimi.nk.framework.core.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nekoimi.nk.framework.core.constant.DateTimeConstants;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Locale;

/**
 * nekoimi  2021/12/16 18:53
 */
@Configuration
public class JacksonConfiguration {

    /**
     * Jackson全局配置
     *
     * @param properties
     * @return
     */
    @Bean
    @Primary
    public JacksonProperties jacksonProperties(JacksonProperties properties) {
        properties.setLocale(Locale.SIMPLIFIED_CHINESE);
        properties.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        properties.getVisibility().put(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        properties.setDateFormat(DateTimeConstants.DEFAULT_DATE_FORMAT);
        properties.setTimeZone(DateTimeConstants.DEFAULT_TIME_ZONE);
        properties.getDeserialization().put(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        properties.getDeserialization().put(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        properties.getSerialization().put(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        properties.getSerialization().put(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        properties.getSerialization().put(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS, true);
        properties.getSerialization().put(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        properties.getSerialization().put(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
        return properties;
    }
}
