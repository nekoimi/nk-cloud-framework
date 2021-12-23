package com.nekoimi.nk.devtools.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * nekoimi  2021/12/23 17:22
 */
@RestController
public class IndexController {

    @GetMapping("/")
    public Mono<Void> index(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FOUND);
        response.getHeaders().setLocation(URI.create("/index.html"));
        return Mono.empty();
    }
}
