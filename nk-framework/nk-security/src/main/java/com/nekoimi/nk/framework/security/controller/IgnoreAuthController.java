package com.nekoimi.nk.framework.security.controller;

import com.nekoimi.nk.framework.core.constant.SecurityConstants;
import com.nekoimi.nk.framework.security.result.AuthenticationResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * nekoimi  2021/12/23 10:40
 *
 * 用来生成swagger文档，没有实际作用
 */
@RestController
@Api(tags = "登录认证模块", produces = "application/json", consumes = "application/json")
public class IgnoreAuthController {

    @PostMapping(value = SecurityConstants.LOGIN_PATH)
    @ApiOperation(value = "登录认证接口", response = AuthenticationResult.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "auth_type", value = "认证方式", required = true, paramType = "query", dataType = "Integer")
    })
    public AuthenticationResult login(@RequestBody Object body) {
        return AuthenticationResult.of(null, null);
    }

    @PostMapping(value = SecurityConstants.LOGOUT_PATH)
    @ApiOperation(value = "注销接口")
    public void logout() {
    }
}
