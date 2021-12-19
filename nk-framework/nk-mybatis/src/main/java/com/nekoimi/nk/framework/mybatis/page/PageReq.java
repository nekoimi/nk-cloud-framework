package com.nekoimi.nk.framework.mybatis.page;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;

/**
 * nekoimi  2021/12/10 17:53
 */
@Getter
@Setter
@Slf4j
public class PageReq {
    private static final String PAGE = "p";
    private static final String PAGE_SIZE = "ps";
    private static final String SORT = "st";
    private static final String ORDER = "od";

    private static final String ORDER_ASC = "asc";

    public static <T> Page<T> buildFromRequest(ServerHttpRequest request) {
        MultiValueMap<String, String> params = request.getQueryParams();
        if (log.isDebugEnabled()) {
            params.toSingleValueMap().forEach((key, value) -> log.debug("{} -> {}", key, value));
        }
        Page<T> resultPage = new Page<>();
        int page = getInt(params, PAGE, "1");
        int pageSize = getInt(params, PAGE_SIZE, "10");
        String sort = get(params, SORT, "");
        String order = get(params, ORDER, ORDER_ASC);
        resultPage.setCurrent(page);
        resultPage.setSize(pageSize);
        if (StringUtils.isNotBlank(sort)) {
            if (ORDER_ASC.equalsIgnoreCase(order)) {
                resultPage.addOrder(OrderItem.asc(sort));
            } else {
                resultPage.addOrder(OrderItem.desc(sort));
            }
        }
        return resultPage;
    }

    /**
     * @param data
     * @param key
     * @param defaultValue
     * @return
     */
    private static String get(MultiValueMap<String, String> data, String key, String defaultValue) {
        if (data.containsKey(key)) {
            return data.getFirst(key);
        }
        return defaultValue;
    }

    /**
     * @param data
     * @param key
     * @param defaultValue
     * @return
     */
    private static int getInt(MultiValueMap<String, String> data, String key, String defaultValue) {
        String value = get(data, key, defaultValue);
        if (!StringUtils.isNumeric(value)) {
            value = defaultValue;
        }
        return Integer.parseInt(value);
    }
}
