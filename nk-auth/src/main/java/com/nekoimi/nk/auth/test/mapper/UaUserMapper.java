package com.nekoimi.nk.auth.test.mapper;

import com.nekoimi.nk.framework.mybatis.mapper.BaseMapper;
import com.nekoimi.nk.auth.test.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * User Mapper
 *
 * nekoimi  2022-01-16
 *
 * This code is generated using nk-devtools.
 */
@Mapper
@Component
public interface UaUserMapper extends BaseMapper<User> {
}
