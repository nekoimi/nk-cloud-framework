package com.nekoimi.nk.framework.mybatis.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.nekoimi.nk.framework.core.contract.IdGenerator;
import com.nekoimi.nk.framework.mybatis.mapper.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * nekoimi  2021/6/13 上午12:03
 */
public abstract class ReactiveBaseService<M extends BaseMapper<E>, E> {
    @Autowired
    protected M mapper;
    @Autowired
    protected IdGenerator idGenerator;

    public Class<E> modelClazz() {
        Class<E> clazz = null;
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
            if (actualTypeArguments != null && actualTypeArguments.length > 1) {
                clazz = (Class<E>) actualTypeArguments[1];
            }
        }
        return clazz;
    }

    @Transactional(readOnly = true)
    public Mono<Boolean> existsById(String id) {
        return Mono.fromCallable(() -> mapper.selectById(id) != null).subscribeOn(Schedulers.elastic());
    }

    @Transactional(readOnly = true)
    public Mono<Boolean> existsBy(Wrapper<E> wrapper) {
        return Mono.fromCallable(() -> mapper.selectCount(wrapper) > 0).subscribeOn(Schedulers.elastic());
    }
}
