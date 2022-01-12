package com.nekoimi.nk.framework.core.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nekoimi.nk.framework.core.utils.JsonUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@ApiModel(description = "响应结构")
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class JsonResp implements Serializable {
    private final static String MESSAGE_OK = "ok";
    private final static Map<String, String> EMPTY_DATA = new HashMap<>();

    @ApiModelProperty(value = "业务状态码；0 - 表示接口返回成功，其他值表示操作不成功，具体消息由msg字段表示")
    private Integer code;
    @ApiModelProperty(value = "业务消息；当且仅当code不为0时有效")
    private String msg;
    @ApiModelProperty(value = "业务数据")
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

    public static JsonResp build(int code, String message, Object data) {
        return new JsonResp(code, message, data);
    }

    @Override
    public String toString() {
        return JsonUtils.write(this);
    }
}
