package com.nekoimi.nk.auth.user.controller;




import com.nekoimi.nk.framework.core.protocol.JsonResp;
import com.nekoimi.nk.auth.user.entity.User;
import com.nekoimi.nk.auth.user.service.UserService;
import com.nekoimi.nk.framework.mybatis.page.PageReq;
import org.springframework.http.server.reactive.ServerHttpRequest;
import com.nekoimi.nk.framework.mybatis.page.PageResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;
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
    @GetMapping("ua/v1/user/list")
    @ApiOperation(value = "获取用户信息分页列表", response = PageResult.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "p", value = "分页页码", required = false, defaultValue = "1", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "ps", value = "每页显示条数", required = false, defaultValue = "10", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "st", value = "排序字段", required = false, defaultValue = "", paramType = "query", dataType = "String",
                    allowableValues = "id,created_at,updated_at,deleted_at,username,password,mobile,email,tenant_id,enable"),
            @ApiImplicitParam(name = "od", value = "排序规则", required = false, defaultValue = "", paramType = "query", dataType = "String", allowableValues = "asc,desc")
    })
    public Mono<JsonResp> list(@ApiIgnore ServerHttpRequest request) {
        return service.page(PageReq.buildFromRequest(request)).map(JsonResp::ok);
    }

    /**
     * 根据id获取用户信息数据
     *
     * @param id
     * @return
     */
    @GetMapping("ua/v1/user/{id}")
    @ApiOperation(value = "根据id获取用户信息数据", response = User.class)
    public Mono<JsonResp> get(@PathVariable("id") String id) {
        return service.getByIdOrFail(id).map(JsonResp::ok);
    }

    /**
     * 添加用户信息数据
     *
     * @return
     */
    @PostMapping("ua/v1/user")
    @ApiOperation(value = "添加用户信息数据", response = User.class)
    public Mono<JsonResp> save(@Validated @RequestBody User body) {
        return service.save(body).flatMap(service::getByIdOrFail).map(JsonResp::ok);
    }

    /**
     * 根据id更新用户信息所有字段
     *
     * @param body
     * @return
     */
    @PutMapping("ua/v1/user/{id}/full")
    @ApiOperation(value = "根据id更新用户信息所有字段", response = JsonResp.class)
    public Mono<JsonResp> updateFull(@PathVariable("id") String id, @Validated @RequestBody User body) {
        return service.updateById(id, body).map(JsonResp::ok);
    }

    /**
     * 根据id更新用户信息部分字段
     *
     * @param body
     * @return
     */
    @PutMapping("ua/v1/user/{id}")
    @ApiOperation(value = "根据id更新用户信息部分字段", response = JsonResp.class)
    @ApiImplicitParams({
                                                            @ApiImplicitParam(name = "username", value = "用户名", required = false, paramType = "body", dataType = "String"),
                                                                                        @ApiImplicitParam(name = "password", value = "密码", required = false, paramType = "body", dataType = "String"),
                                                                                        @ApiImplicitParam(name = "mobile", value = "手机号", required = false, paramType = "body", dataType = "String"),
                                                                                        @ApiImplicitParam(name = "email", value = "邮箱", required = false, paramType = "body", dataType = "String"),
                                                                                        @ApiImplicitParam(name = "tenantId", value = "租户ID", required = false, paramType = "body", dataType = "String"),
                                                                                        @ApiImplicitParam(name = "enable", value = "是否启用；1 - enable，0 - disable", required = false, paramType = "body", dataType = "Boolean")
                                        })
    public Mono<JsonResp> updateFull(@PathVariable("id") String id, @ApiIgnore @RequestBody Map<String, Object> body) {
        return service.updateById(id, body).map(JsonResp::ok);
    }

    /**
     * 批量更新用户信息数据
     *
     * @param body
     * @return
     */
    @PutMapping("ua/v1/user/batch")
    @ApiOperation(value = "批量更新用户信息数据", response = JsonResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "id的列表，使用,分割", required = true, example = "1,2,3", paramType = "query", dataType = "String"),
                                                            @ApiImplicitParam(name = "username", value = "用户名", required = false, paramType = "body", dataType = "String"),
                                                                                        @ApiImplicitParam(name = "password", value = "密码", required = false, paramType = "body", dataType = "String"),
                                                                                        @ApiImplicitParam(name = "mobile", value = "手机号", required = false, paramType = "body", dataType = "String"),
                                                                                        @ApiImplicitParam(name = "email", value = "邮箱", required = false, paramType = "body", dataType = "String"),
                                                                                        @ApiImplicitParam(name = "tenantId", value = "租户ID", required = false, paramType = "body", dataType = "String"),
                                                                                        @ApiImplicitParam(name = "enable", value = "是否启用；1 - enable，0 - disable", required = false, paramType = "body", dataType = "Boolean")
                                        })
    public Mono<JsonResp> updateBatch(@RequestParam("ids") String ids, @ApiIgnore @RequestBody Map<String, Object> body) {
        return service.updateBatch(ids, body).map(JsonResp::ok);
    }

    /**
     * 根据id删除用户信息数据
     *
     * @param id
     * @return
     */
    @DeleteMapping("ua/v1/user/{id}")
    @ApiOperation(value = "根据id删除用户信息数据", response = JsonResp.class)
    public Mono<JsonResp> remove(@PathVariable("id") String id) {
        return service.removeById(id).map(JsonResp::ok);
    }

    /**
     * 批量删除用户信息数据
     *
     * @return
     */
    @DeleteMapping("ua/v1/user/batch")
    @ApiOperation(value = "批量删除用户信息数据", response = JsonResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "id的列表，使用,分割", required = true, example = "1,2,3", paramType = "query", dataType = "String")
    })
    public Mono<JsonResp> removeBatch(@RequestParam(value = "ids") String ids) {
        return service.removeBatch(ids).map(JsonResp::ok);
    }

}
