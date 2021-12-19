package com.nekoimi.nk.auth.controller;

import com.nekoimi.nk.auth.service.UaUserService;
import com.nekoimi.nk.framework.core.protocol.JsonResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
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
        return userService.exists(id).map(JsonResp::ok);
    }

    @GetMapping("/list")
    public Mono<JsonResp> list() {
        return userService.findAll().collectList().map(JsonResp::ok);
    }

    @GetMapping("/get/{id}")
    public Mono<JsonResp> get(@PathVariable String id) {
        return userService.getById(id).map(JsonResp::ok);
    }

    @GetMapping("/return/string")
    public String returnString() {
        return "string";
    }

    @GetMapping("/return/mono/string")
    public Mono<String> returnMonoString() {
        return Mono.just("string");
    }

    @GetMapping("/return/mono/void")
    public Mono<Void> returnMonoVoid() {
        return Mono.empty();
    }

    @GetMapping("/return/flux/void")
    public Flux<Void> returnFluxVoid() {
        return Flux.empty();
    }

    @GetMapping("/return/flux/string")
    public Flux<String> returnFluxString() {
        return Flux.just("AAA", "BBB", "CCC");
    }
}
