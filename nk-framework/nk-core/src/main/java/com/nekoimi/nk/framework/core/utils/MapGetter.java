package com.nekoimi.nk.framework.core.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;
import java.util.Objects;

/**
 * nekoimi  2022/1/9 21:22
 */
@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class MapGetter {
    private final Map<String, Object> map;

    public String str(String key) {
        MapUtils.getString(map, key);
        return Objects.toString(map.getOrDefault(key, ""));
    }
}
