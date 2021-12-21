package com.nekoimi.nk.gateway.controller;

import com.nekoimi.nk.framework.core.protocol.JsonResp;
import lombok.Data;
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

    @RequestMapping("/home")
    public JsonResp hello() {
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
