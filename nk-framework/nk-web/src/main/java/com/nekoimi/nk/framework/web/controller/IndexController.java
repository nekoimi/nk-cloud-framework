package com.nekoimi.nk.framework.web.controller;

import com.nekoimi.nk.framework.core.protocol.JsonResp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/15 8:58
 */
@RestController
public class IndexController {
    @Value("${spring.application.name}")
    private String name;

    @RequestMapping("/")
    public Mono<JsonResp> index() {
        return Mono.fromCallable(() -> JsonResp.ok("hello, " + name));
    }
}
