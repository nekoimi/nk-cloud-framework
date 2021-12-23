package com.nekoimi.nk.platform.uc.service.controller;


import com.nekoimi.nk.framework.mybatis.page.PageReq;
import com.nekoimi.nk.framework.mybatis.page.PageResult;
import com.nekoimi.nk.platform.uc.service.entity.User;
import com.nekoimi.nk.platform.uc.service.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * User Controller
 * <p>
 * nekoimi  2021-12-23
 * <p>
 * This code is generated using nk-devtools.
 */
@RestController
@Api(tags = "v1用户模块", produces = "application/json", consumes = "application/json")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    /**
     * 获取用户信息分页列表
     *
     * @return
     */
    @GetMapping("/v1/user/list")
    @ApiOperation(value = "获取用户信息分页列表", response = PageResult.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "p", value = "分页页码", required = false, defaultValue = "1", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "ps", value = "每页显示条数", required = false, defaultValue = "10", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "st", value = "排序字段", required = false, defaultValue = "", paramType = "query", dataType = "String",
                    allowableValues = "id,created_at,updated_at,deleted_at,username,password,mobile,email,tenant_id,enable"),
            @ApiImplicitParam(name = "od", value = "排序规则", required = false, defaultValue = "", paramType = "query", dataType = "String",
                    allowableValues = "asc,desc")
    })
    public Mono<PageResult<User>> list(@ApiIgnore ServerHttpRequest request) {
        return service.page(PageReq.buildFromRequest(request));
    }

    /**
     * 根据id获取用户信息数据
     *
     * @param id
     * @return
     */
    @GetMapping("/v1/user/{id}")
    @ApiOperation(value = "根据id获取用户信息数据", response = User.class)
    public Mono<User> get(@PathVariable("id") String id) {
        return service.getByIdOrFail(id);
    }

    /**
     * 添加用户信息数据
     *
     * @return
     */
    @PostMapping("/v1/user")
    @ApiOperation(value = "添加用户信息数据", response = User.class)
    public Mono<User> save(@Validated @RequestBody User body) {
        return service.save(body).flatMap(service::getByIdOrFail);
    }

    /**
     * 根据id更新用户信息所有字段
     *
     * @param body
     * @return
     */
    @PutMapping("/v1/user/{id}/full")
    @ApiOperation(value = "根据id更新用户信息所有字段", response = Boolean.class)
    public Mono<Boolean> updateFull(@PathVariable("id") String id, @Validated @RequestBody User body) {
        return service.updateById(id, body);
    }

    /**
     * 根据id更新用户信息部分字段
     *
     * @param body
     * @return
     */
    @PutMapping("/v1/user/{id}")
    @ApiOperation(value = "根据id更新用户信息部分字段", response = Boolean.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = false, paramType = "body", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = false, paramType = "body", dataType = "String"),
            @ApiImplicitParam(name = "mobile", value = "手机号", required = false, paramType = "body", dataType = "String"),
            @ApiImplicitParam(name = "email", value = "邮箱", required = false, paramType = "body", dataType = "String"),
            @ApiImplicitParam(name = "tenantId", value = "租户ID", required = false, paramType = "body", dataType = "String"),
            @ApiImplicitParam(name = "enable", value = "是否启用；1 - enable，0 - disable", required = false, paramType = "body", dataType = "Boolean")
    })
    public Mono<Boolean> updateFull(@PathVariable("id") String id, @ApiIgnore @RequestBody Map<String, Object> body) {
        return service.updateById(id, body);
    }

    /**
     * 批量更新用户信息数据
     *
     * @param body
     * @return
     */
    @PutMapping("/v1/user/batch")
    @ApiOperation(value = "批量更新用户信息数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "id的列表，使用,分割", required = true, example = "1,2,3", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "username", value = "用户名", required = false, paramType = "body", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = false, paramType = "body", dataType = "String"),
            @ApiImplicitParam(name = "mobile", value = "手机号", required = false, paramType = "body", dataType = "String"),
            @ApiImplicitParam(name = "email", value = "邮箱", required = false, paramType = "body", dataType = "String"),
            @ApiImplicitParam(name = "tenantId", value = "租户ID", required = false, paramType = "body", dataType = "String"),
            @ApiImplicitParam(name = "enable", value = "是否启用；1 - enable，0 - disable", required = false, paramType = "body", dataType = "Boolean")
    })
    public Mono<Void> updateBatch(@RequestParam("ids") String ids, @ApiIgnore @RequestBody Map<String, Object> body) {
        return service.updateBatch(ids, body);
    }

    /**
     * 根据id删除用户信息数据
     *
     * @param id
     * @return
     */
    @DeleteMapping("/v1/user/{id}")
    @ApiOperation(value = "根据id删除用户信息数据")
    public Mono<Void> remove(@PathVariable("id") String id) {
        return service.removeById(id);
    }

    /**
     * 批量删除用户信息数据
     *
     * @return
     */
    @DeleteMapping("/v1/user/batch")
    @ApiOperation(value = "批量删除用户信息数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "id的列表，使用,分割", required = true, example = "1,2,3", paramType = "query", dataType = "String")
    })
    public Mono<Void> removeBatch(@RequestParam(value = "ids") String ids) {
        return service.removeBatch(ids);
    }

}
