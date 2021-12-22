package com.nekoimi.nk.auth.user.service.impl;

import com.nekoimi.nk.framework.mybatis.service.impl.ReactiveCrudServiceImpl;
import com.nekoimi.nk.auth.user.entity.User;
import com.nekoimi.nk.auth.user.mapper.UserMapper;
import com.nekoimi.nk.auth.user.service.UserService;
import org.springframework.stereotype.Service;

/**
 * User Service
 *
 * nekoimi  2021-12-22
 *
 * This code is generated using nk-devtools.
 */
@Service
public class UserServiceImpl extends ReactiveCrudServiceImpl<UserMapper, User> implements UserService {
}
