package com.nekoimi.nk.auth.controller;

import com.nekoimi.nk.framework.core.utils.JsonUtils;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;

/**
 * nekoimi  2021/12/17 16:45
 */
@RestController
public class JwkController {
//    @Autowired
//    private KeyPair keyPair;
//
//    @GetMapping("/pub-key/jwk.json")
//    public Mono<String> key() {
//        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//        RSAKey key = new RSAKey.Builder(publicKey).build();
//        return Mono.just(JsonUtils.write(new JWKSet(key).toJSONObject()));
//    }
}
