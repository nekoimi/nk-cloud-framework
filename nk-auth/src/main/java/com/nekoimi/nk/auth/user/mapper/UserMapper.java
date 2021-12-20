package com.nekoimi.nk.auth.user.mapper;

import com.nekoimi.nk.framework.mybatis.mapper.BaseMapper;
import com.nekoimi.nk.auth.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * User Mapper
 *
 * nekoimi  2021-12-20
 *
 * This code is generated using nk-devtools.
 */
@Mapper
@Component
public interface UserMapper extends BaseMapper<User> {
}
