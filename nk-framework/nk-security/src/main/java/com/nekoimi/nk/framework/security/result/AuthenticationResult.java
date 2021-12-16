package com.nekoimi.nk.framework.security.result;

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
public class AuthenticationResult {
    private String token;
    private Map<String, Object> info;
}
