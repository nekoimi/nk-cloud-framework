package com.nekoimi.nk.auth.service.impl;

import com.nekoimi.nk.auth.entity.UaUser;
import com.nekoimi.nk.auth.mapper.UaUserMapper;
import com.nekoimi.nk.auth.service.UaUserService;
import com.nekoimi.nk.framework.mybatis.service.impl.ReactiveCrudServiceImpl;
import org.springframework.stereotype.Service;

/**
 * nekoimi  2021/12/17 22:57
 */
@Service
public class UaUserServiceImpl extends ReactiveCrudServiceImpl<UaUserMapper, UaUser> implements UaUserService {
}
