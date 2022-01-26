package com.nekoimi.nk.auth.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2022/1/21 15:28
 */
@RestController
public class CaptchaController {

    @GetMapping("/captcha/create")
    public Mono<Void> createCaptcha() {
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(100, 100);
        captcha.verify("");
        return Mono.empty();
    }
}
