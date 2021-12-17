package com.nekoimi.nk.auth.service.impl;

import com.nekoimi.nk.auth.entity.UaUser;
import com.nekoimi.nk.auth.mapper.UaUserMapper;
import com.nekoimi.nk.auth.service.UaUserService;
import com.nekoimi.nk.framework.mybatis.service.ReactiveBaseService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/17 22:57
 */
@Service
public class UaUserServiceImpl extends ReactiveBaseService<UaUserMapper, UaUser> implements UaUserService {
    @Override
    public Mono<Boolean> existsBy(String id) {
        return existsById(id);
    }
}
