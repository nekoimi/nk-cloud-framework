package com.nekoimi.nk.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.nekoimi.nk.auth.entity.UaUser;
import com.nekoimi.nk.auth.service.UaUserService;
import com.nekoimi.nk.framework.core.protocol.JsonResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/17 22:59
 */
@RestController
public class UaUserController {
    @Autowired
    private UaUserService userService;

//    @GetMapping("/exists/{id}")
//    public Mono<JsonResp> exists(@PathVariable String id) {
////        return userService.existsBy(id).map(JsonResp::ok);
//    }

    @GetMapping("/get/{id}")
    public Mono<JsonResp> get(@PathVariable String id) {
        LambdaQueryWrapper<Object> query = Wrappers.lambdaQuery();
        userService.getOf(UaUser::getUsername, "");
        return Mono.empty().map(JsonResp::ok);
    }
}
