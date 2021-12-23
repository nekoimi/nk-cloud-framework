package com.nekoimi.nk.platform.uc.service.service.impl;

import com.nekoimi.nk.framework.mybatis.service.impl.ReactiveCrudServiceImpl;
import com.nekoimi.nk.platform.uc.model.entity.User;
import com.nekoimi.nk.platform.uc.service.mapper.UserMapper;
import com.nekoimi.nk.platform.uc.service.service.UserService;
import org.springframework.stereotype.Service;

/**
 * User Service
 *
 * nekoimi  2021-12-23
 *
 * This code is generated using nk-devtools.
 */
@Service
public class UserServiceImpl extends ReactiveCrudServiceImpl<UserMapper, User> implements UserService {
}
