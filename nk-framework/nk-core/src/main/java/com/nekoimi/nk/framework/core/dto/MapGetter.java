package com.nekoimi.nk.framework.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
        return Objects.toString(map.getOrDefault(key, ""));
    }
}
