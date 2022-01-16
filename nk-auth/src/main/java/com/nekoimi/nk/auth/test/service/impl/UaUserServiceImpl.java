package com.nekoimi.nk.auth.test.service.impl;

import com.nekoimi.nk.framework.mybatis.service.impl.ReactiveCrudServiceImpl;
import com.nekoimi.nk.auth.test.entity.User;
import com.nekoimi.nk.auth.test.mapper.UaUserMapper;
import com.nekoimi.nk.auth.test.service.UaUserService;
import org.springframework.stereotype.Service;

/**
 * User Service
 *
 * nekoimi  2022-01-16
 *
 * This code is generated using nk-devtools.
 */
@Service
public class UaUserServiceImpl extends ReactiveCrudServiceImpl<UaUserMapper, User> implements UaUserService {
}
