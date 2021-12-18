package com.nekoimi.nk.framework.mybatis.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.ResultHandler;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Nekoimi  2020/7/22 上午10:21
 * @see com.baomidou.mybatisplus.core.mapper.BaseMapper
 *
 * TODO 自己在此扩展常用查询接口
 */
public interface BaseMapper<E> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<E> {

    void selectListWithHandler(@Param(Constants.WRAPPER) Wrapper<E> queryWrapper, ResultHandler<E> handler);

    void selectBatchIdsWithHandler(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList, ResultHandler<E> handler);
}
