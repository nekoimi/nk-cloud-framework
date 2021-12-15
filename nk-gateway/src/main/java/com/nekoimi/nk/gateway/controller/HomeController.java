package com.nekoimi.nk.gateway.controller;

import com.nekoimi.nk.framework.core.protocol.JsonResp;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * nekoimi  2021/12/14 11:55
 */
@RestController
public class HomeController {
    @Autowired
    private PasswordEncoder encoder;

    @RequestMapping("/home")
    public JsonResp hello() {
        String encode = encoder.encode("ky123456");
        System.out.println(encode);

        User user = new User();
        user.setUsername("");
        user.setDate(new Date());
        user.setLocalDate(LocalDate.now());
        user.setLocalDateTime(LocalDateTime.now());
        return JsonResp.ok(user);
    }

    @Data
    class User {
        private int age;
        private Integer age2;
        private String username;
        private Date date;
        private LocalDate localDate;
        private LocalDateTime localDateTime;
    }
}
