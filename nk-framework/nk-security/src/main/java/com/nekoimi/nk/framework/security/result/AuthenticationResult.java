package com.nekoimi.nk.framework.security.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * nekoimi  2021/12/16 20:12
 *
 * 用户登录返回信息
 */
@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@ApiModel(description = "认证返回结构")
public class AuthenticationResult {
    @ApiModelProperty("token")
    private String token;
    @ApiModelProperty("认证返回信息")
    private Map<String, Object> info;
}
