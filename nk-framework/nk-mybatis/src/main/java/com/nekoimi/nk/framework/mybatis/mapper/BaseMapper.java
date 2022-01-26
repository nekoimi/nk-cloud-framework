package com.nekoimi.nk.framework.mybatis.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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

    /**
     * 查询列表
     * @param queryWrapper
     * @param handler
     */
    void selectListWithHandler(@Param(Constants.WRAPPER) Wrapper<E> queryWrapper, ResultHandler<E> handler);

    /**
     * 根据idList查询列表
     * @param idList
     * @param handler
     */
    void selectBatchIdsWithHandler(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList, ResultHandler<E> handler);

    /**
     * 查询分页列表
     * @param page
     * @param queryWrapper
     * @param handler
     * @param <P>
     */
    <P extends IPage<E>> void selectPageWithHandler(P page, @Param(Constants.WRAPPER) Wrapper<E> queryWrapper, ResultHandler<E> handler);
}
