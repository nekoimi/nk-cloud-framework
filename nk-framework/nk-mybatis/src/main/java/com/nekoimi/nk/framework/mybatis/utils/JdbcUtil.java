package com.nekoimi.nk.framework.mybatis.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * nekoimi  2021/12/14 15:44
 */
public class JdbcUtil {

    public static String getDbName(String url) {
        // jdbc:mysql://localhost:3306/test?
        Pattern pattern = Pattern.compile("^jdbc:.*//.*:[0-9]*/(.*)");
        Matcher matcher = pattern.matcher(url);
        if (!matcher.find()) {
            return null;
        }
        String result = matcher.group(1);
        if (StringUtils.isNotBlank(result)) {
            if (result.contains("?")) {
                String[] strings = result.split("[?]");
                return strings[0];
            }
            return result;
        }
        return null;
    }
}
