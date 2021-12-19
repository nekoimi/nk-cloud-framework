package com.nekoimi.nk.framework.core.protocol;

import com.nekoimi.nk.framework.core.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * nekoimi  2021/12/6 11:46
 */
@Slf4j
@Getter
@Setter
@AllArgsConstructor(staticName = "build")
public class JsonResp implements Serializable {
    private final static String MESSAGE_OK = "ok";
    private final static Map<String, String> EMPTY_DATA = new HashMap<>();
    private Integer code;
    private String msg;
    private Object data;

    public static JsonResp ok() {
        return ok(EMPTY_DATA);
    }

    public static JsonResp ok(Object data) {
        return JsonResp.build(0, MESSAGE_OK, data);
    }

    public static JsonResp error(int code, String message) {
        return JsonResp.build(code, message, null);
    }

    @Override
    public String toString() {
        return JsonUtils.write(this);
    }
}
