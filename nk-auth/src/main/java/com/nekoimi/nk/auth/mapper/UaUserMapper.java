package com.nekoimi.nk.auth.mapper;

import com.nekoimi.nk.auth.entity.UaUser;
import com.nekoimi.nk.framework.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.ResultHandler;

/**
 * nekoimi  2021/12/17 22:56
 */
@Mapper
public interface UaUserMapper extends BaseMapper<UaUser> {

    @Select("select * from ua_user")
    @ResultType(UaUser.class)
    void selectUaUserList(ResultHandler<UaUser> handler);
}
