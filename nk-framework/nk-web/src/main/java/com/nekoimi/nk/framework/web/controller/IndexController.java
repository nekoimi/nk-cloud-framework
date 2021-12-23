package com.nekoimi.nk.framework.web.controller;

import com.nekoimi.nk.framework.core.protocol.JsonResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/15 8:58
 */
@Api(tags = "Hello World", produces = "application/json", consumes = "application/json")
public class IndexController extends EmptyController {
    @Value("${spring.application.name}")
    private String name;

    @GetMapping("/")
    @ApiOperation(value = "index", response = JsonResp.class)
    public Mono<JsonResp> index() {
        return Mono.fromCallable(() -> JsonResp.ok("hello, " + name));
    }
}
