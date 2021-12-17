package com.nekoimi.nk.auth.controller;

import com.nekoimi.nk.framework.core.protocol.JsonResp;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/17 10:50
 */
@RestController
public class HomeController {

    @RequestMapping("/home")
    public Mono<JsonResp> home() {
        return Mono.just(JsonResp.ok("fdsfsdfsdfsd"));
    }
}
