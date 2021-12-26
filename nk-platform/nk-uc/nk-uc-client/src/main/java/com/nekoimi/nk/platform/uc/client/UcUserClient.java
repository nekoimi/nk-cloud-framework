package com.nekoimi.nk.platform.uc.client;

import com.nekoimi.nk.platform.uc.model.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * nekoimi  2021/12/23 16:21
 */
@FeignClient(name = "nk-uc-service")
public interface UcUserClient {

    /**
     * 根据用户ID获取用户信息
     * @param id
     * @return
     */
    @GetMapping("/v1/user/{id}")
    User getById(@PathVariable String id);
}
