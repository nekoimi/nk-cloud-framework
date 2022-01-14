package com.nekoimi.nk.framework.mybatis.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.nekoimi.nk.framework.mybatis.collect.QMap;
import com.nekoimi.nk.framework.mybatis.mapper.BaseMapper;
import com.nekoimi.nk.framework.mybatis.page.PageResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * nekoimi  2021/12/18 13:59
 *
 * CRUD service interface
 */
public interface ReactiveCrudService<E> {
    Class<E> getEntityClass();
    BaseMapper<E> getMapper();
    QMap<SFunction<E, Object>, Object> queryMap();
    LambdaQueryWrapper<E> lambdaQuery();
    LambdaUpdateWrapper<E> lambdaUpdate();
    Mono<E> getById(Serializable id);
    Mono<E> getByQuery(Consumer<LambdaQueryWrapper<E>> consumer);
    Mono<E> getByMap(Consumer<QMap<SFunction<E, Object>, Object>> consumer);
    Mono<E> getOf(SFunction<E, Object> k1, Object v1);
    Mono<E> getOf(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2);
    Mono<E> getOf(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3);
    Mono<E> getOf(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4);
    Mono<E> getOf(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5);
    Mono<E> getOf(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6);
    Mono<E> getOf(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6, SFunction<E, Object> k7, Object v7);
    Mono<E> getOf(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6, SFunction<E, Object> k7, Object v7, SFunction<E, Object> k8, Object v8);
    Mono<E> getOf(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6, SFunction<E, Object> k7, Object v7, SFunction<E, Object> k8, Object v8, SFunction<E, Object> k9, Object v9);
    Mono<E> getOf(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6, SFunction<E, Object> k7, Object v7, SFunction<E, Object> k8, Object v8, SFunction<E, Object> k9, Object v9, SFunction<E, Object> k10, Object v10);
    Mono<E> getByIdOrFail(Serializable id);
    Mono<E> getByQueryOrFail(Consumer<LambdaQueryWrapper<E>> consumer);
    Mono<E> getByMapOrFail(Consumer<QMap<SFunction<E, Object>, Object>> consumer);
    Mono<E> getOfOrFail(SFunction<E, Object> k1, Object v1);
    Mono<E> getOfOrFail(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2);
    Mono<E> getOfOrFail(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3);
    Mono<E> getOfOrFail(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4);
    Mono<E> getOfOrFail(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5);
    Mono<E> getOfOrFail(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6);
    Mono<E> getOfOrFail(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6, SFunction<E, Object> k7, Object v7);
    Mono<E> getOfOrFail(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6, SFunction<E, Object> k7, Object v7, SFunction<E, Object> k8, Object v8);
    Mono<E> getOfOrFail(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6, SFunction<E, Object> k7, Object v7, SFunction<E, Object> k8, Object v8, SFunction<E, Object> k9, Object v9);
    Mono<E> getOfOrFail(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6, SFunction<E, Object> k7, Object v7, SFunction<E, Object> k8, Object v8, SFunction<E, Object> k9, Object v9, SFunction<E, Object> k10, Object v10);
    Mono<Boolean> exists(Serializable id);
    Mono<Boolean> existsByQuery(Consumer<LambdaQueryWrapper<E>> consumer);
    Mono<Boolean> existsByMap(Consumer<QMap<SFunction<E, Object>, Object>> consumer);
    Mono<Serializable> save(E entity);
    Mono<Serializable> saveMap(Map<String, Object> map);
    Mono<Void> saveBatch(List<E> entityList);
    Mono<Serializable> saveOrUpdate(E entity);
    Mono<Boolean> update(E entity);
    Mono<Void> updateBatch(String ids, Map<String, Object> map);
    Mono<Void> updateBatch(List<? extends Serializable> idList, Map<String, Object> map);
    Mono<Boolean> updateByQuery(E entity, Consumer<LambdaQueryWrapper<E>> consumer);
    Mono<Boolean> updateById(Serializable id, E entity);
    Mono<Boolean> updateById(Serializable id, Map<String, Object> map);
    Mono<Boolean> updateById(Serializable id, Consumer<LambdaUpdateWrapper<E>> consumer);
    Mono<Boolean> updateByIdOfMap(Serializable id, Consumer<QMap<SFunction<E, Object>, Object>> consumer);
    Mono<Boolean> updateOf(Serializable id, SFunction<E, Object> k1, Object v1);
    Mono<Boolean> updateOf(Serializable id, SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2);
    Mono<Boolean> updateOf(Serializable id, SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3);
    Mono<Boolean> updateOf(Serializable id, SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4);
    Mono<Boolean> updateOf(Serializable id, SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5);
    Mono<Boolean> updateOf(Serializable id, SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6);
    Mono<Boolean> updateOf(Serializable id, SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6, SFunction<E, Object> k7, Object v7);
    Mono<Boolean> updateOf(Serializable id, SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6, SFunction<E, Object> k7, Object v7, SFunction<E, Object> k8, Object v8);
    Mono<Boolean> updateOf(Serializable id, SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6, SFunction<E, Object> k7, Object v7, SFunction<E, Object> k8, Object v8, SFunction<E, Object> k9, Object v9);
    Mono<Boolean> updateOf(Serializable id, SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6, SFunction<E, Object> k7, Object v7, SFunction<E, Object> k8, Object v8, SFunction<E, Object> k9, Object v9, SFunction<E, Object> k10, Object v10);
    Mono<Void> removeById(Serializable id);
    Mono<Void> removeByQuery(Consumer<LambdaQueryWrapper<E>> consumer);
    Mono<Void> removeBatch(String ids);
    Mono<Void> removeBatch(List<? extends Serializable> idList);
    Mono<Long> countAll();
    Mono<Long> countByQuery(Consumer<LambdaQueryWrapper<E>> consumer);
    Mono<Long> countByMap(Consumer<QMap<SFunction<E, Object>, Object>> consumer);
    Flux<E> findAll();
    Flux<E> findByIds(List<? extends Serializable> ids);
    Flux<E> findByQuery(Consumer<LambdaQueryWrapper<E>> consumer);
    Flux<E> findByMap(Consumer<QMap<SFunction<E, Object>, Object>> consumer);
    @SuppressWarnings("unchecked")
    Flux<E> findAllSelectColumn(SFunction<E, Object>...columns);
    @SuppressWarnings("unchecked")
    Flux<E> findByQuerySelectColumn(Consumer<LambdaQueryWrapper<E>> consumer, SFunction<E, Object>... columns);
    @SuppressWarnings("unchecked")
    Flux<E> findByMapSelectColumn(Consumer<QMap<SFunction<E, Object>, Object>> consumer, SFunction<E, Object>... columns);
    Flux<E> findOf(SFunction<E, Object> k1, Object v1);
    Flux<E> findOf(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2);
    Flux<E> findOf(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3);
    Flux<E> findOf(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4);
    Flux<E> findOf(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5);
    Flux<E> findOf(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6);
    Flux<E> findOf(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6, SFunction<E, Object> k7, Object v7);
    Flux<E> findOf(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6, SFunction<E, Object> k7, Object v7, SFunction<E, Object> k8, Object v8);
    Flux<E> findOf(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6, SFunction<E, Object> k7, Object v7, SFunction<E, Object> k8, Object v8, SFunction<E, Object> k9, Object v9);
    Flux<E> findOf(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6, SFunction<E, Object> k7, Object v7, SFunction<E, Object> k8, Object v8, SFunction<E, Object> k9, Object v9, SFunction<E, Object> k10, Object v10);
    Mono<PageResult<E>> page(IPage<E> page);
    @SuppressWarnings("unchecked")
    Mono<PageResult<E>> page(IPage<E> page, SFunction<E, Object>... columns);
    Mono<PageResult<E>> page(IPage<E> page, Consumer<LambdaQueryWrapper<E>> consumer);
}
