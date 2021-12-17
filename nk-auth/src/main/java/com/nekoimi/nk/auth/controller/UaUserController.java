package com.nekoimi.nk.auth.controller;

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

    @GetMapping("/exists/{id}")
    public Mono<JsonResp> exists(@PathVariable String id) {
        return userService.existsBy(id).map(JsonResp::ok);
    }
}
