package com.nekoimi.nk.framework.mybatis.service.impl;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.nekoimi.nk.framework.core.contract.IdGenerator;
import com.nekoimi.nk.framework.core.utils.ClazzUtils;
import com.nekoimi.nk.framework.core.utils.JsonUtils;
import com.nekoimi.nk.framework.mybatis.collect.QueryMap;
import com.nekoimi.nk.framework.mybatis.exception.*;
import com.nekoimi.nk.framework.mybatis.mapper.BaseMapper;
import com.nekoimi.nk.framework.mybatis.page.PageResult;
import com.nekoimi.nk.framework.mybatis.service.ReactiveCrudService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.function.Consumer;

/**
 * nekoimi  2021/6/13 上午12:03
 */
@Slf4j
public abstract class ReactiveCrudServiceImpl<M extends BaseMapper<E>, E> implements ReactiveCrudService<E> {
    @Autowired
    protected M mapper;
    @Autowired
    protected IdGenerator idGenerator;

    public Class<E> entityClazz() {
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

    protected TableInfo tableInfo() {
        TableInfo info = TableInfoHelper.getTableInfo(entityClazz());
        if (info == null || StringUtils.isBlank(info.getKeyColumn())) {
            throw new InvalidParameterException("Table " + entityClazz().getName() + " recover info error! ");
        }
        return info;
    }

    protected LambdaQueryWrapper<E> acceptQueryConsumer(Consumer<LambdaQueryWrapper<E>> consumer) {
        LambdaQueryWrapper<E> query = Wrappers.lambdaQuery(entityClazz());
        consumer.accept(query);
        return query;
    }

    protected LambdaUpdateWrapper<E> acceptUpdateConsumer(Consumer<LambdaUpdateWrapper<E>> consumer) {
        LambdaUpdateWrapper<E> query = Wrappers.lambdaUpdate(entityClazz());
        consumer.accept(query);
        return query;
    }

    protected LambdaQueryWrapper<E> acceptQueryMapConsumer(Consumer<QueryMap<SFunction<E, Object>, Object>> consumer) {
        LambdaQueryWrapper<E> query = Wrappers.lambdaQuery(entityClazz());
        QueryMap<SFunction<E, Object>, Object> queryMap = new QueryMap<>(new HashMap<>());
        consumer.accept(queryMap);
        Set<Map.Entry<SFunction<E, Object>, Object>> entries = queryMap.map().entrySet();
        entries.forEach(entry -> query.ne(entry.getKey(), entry.getValue()));
        return query;
    }

    protected LambdaUpdateWrapper<E> acceptUpdateMapConsumer(Consumer<QueryMap<SFunction<E, Object>, Object>> consumer) {
        LambdaUpdateWrapper<E> update = Wrappers.lambdaUpdate(entityClazz());
        QueryMap<SFunction<E, Object>, Object> queryMap = new QueryMap<>(new HashMap<>());
        consumer.accept(queryMap);
        Set<Map.Entry<SFunction<E, Object>, Object>> entries = queryMap.map().entrySet();
        entries.forEach(entry -> update.set(entry.getKey(), entry.getValue()));
        return update;
    }

    protected Mono<E> checkGetFail(Mono<E> eMono) {
        return eMono.switchIfEmpty(Mono.error(new FailedToResourceNotFoundException()));
    }

    protected Mono<Boolean> checkExists(Mono<E> eMono) {
        return eMono.flatMap(e -> Mono.just(true)).switchIfEmpty(Mono.just(false));
    }

    protected Mono<Boolean> dmlRowToBoolean(Integer rows) {
        return Mono.justOrEmpty(rows).flatMap(i -> Mono.just(i > 0)).switchIfEmpty(Mono.just(false));
    }

    protected Mono<E> mapConvertToEntity(Map<String, Object> map) {
        Class<E> entityClazz = entityClazz();
        E e = null;
        try {
            e = entityClazz.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException rex) {
            log.error(rex.getMessage());
            if (log.isDebugEnabled()) {
                rex.printStackTrace();
            }
            return Mono.error(new FailedToResourceSaveException());
        }
        // fixme 这里返回的数据表字段不包含主键
        List<TableFieldInfo> fieldList = tableInfo().getFieldList();
        try {
            for (TableFieldInfo fieldInfo : fieldList) {
                String propertyName = fieldInfo.getProperty();
                if (map.containsKey(propertyName)) {
                    Object value = map.get(propertyName);
                    Class<?> propertyType = fieldInfo.getPropertyType();
                    if (value instanceof Map<?, ?> || value instanceof Collection<?>) {
                        String json = JsonUtils.write(value);
                        value = JsonUtils.read(json, propertyType);
                    }
                    if (ClazzUtils.instanceOf(propertyType, Map.class) || ClazzUtils.instanceOf(propertyType, Collection.class)) {
                        if (value instanceof String) {
                            String json = (String) value;
                            value = JsonUtils.read(json, propertyType);
                        }
                    }
                    fieldInfo.getField().setAccessible(true);
                    fieldInfo.getField().set(e, value);
                }
            }

            String keyProperty = tableInfo().getKeyProperty();
            if (map.containsKey(keyProperty)) {
                Field keyField = ClazzUtils.findField(entityClazz, keyProperty);
                if (keyField == null) {
                    return Mono.error(new FailedToResourceSaveException("Key property (%s) not found!", keyProperty));
                }
                keyField.setAccessible(true);
                keyField.set(e, map.get(keyProperty));
            }
        } catch (IllegalAccessException ex) {
            log.error(ex.getMessage());
            if (log.isDebugEnabled()) {
                ex.printStackTrace();
            }
            return Mono.error(new FailedToResourceSaveException());
        }
        return Mono.just(e);
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<E> getById(Serializable id) {
        return Mono.fromCallable(() -> mapper.selectById(id))
                .flatMap(Mono::justOrEmpty)
                .subscribeOn(Schedulers.elastic());
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<E> getByQuery(Consumer<LambdaQueryWrapper<E>> consumer) {
        return Mono.just(consumer)
                .map(this::acceptQueryConsumer)
                .publishOn(Schedulers.elastic())
                .map(eqw -> mapper.selectOne(eqw))
                .flatMap(Mono::justOrEmpty);
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<E> getByMap(Consumer<QueryMap<SFunction<E, Object>, Object>> consumer) {
        return Mono.just(consumer)
                .map(this::acceptQueryMapConsumer)
                .publishOn(Schedulers.elastic())
                .map(eqw -> mapper.selectOne(eqw))
                .flatMap(Mono::justOrEmpty);
    }

    @Override
    public Mono<E> getOf(SFunction<E, Object> k1, Object v1) {
        return getByMap(map -> map.put(k1, v1));
    }

    @Override
    public Mono<E> getOf(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2) {
        return getByMap(map -> map.put(k1, v1).put(k2, v2));
    }

    @Override
    public Mono<E> getOf(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3) {
        return getByMap(map -> map.put(k1, v1).put(k2, v2).put(k3, v3));
    }

    @Override
    public Mono<E> getOf(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4) {
        return getByMap(map -> map.put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4));
    }

    @Override
    public Mono<E> getOf(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5) {
        return getByMap(map -> map.put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5));
    }

    @Override
    public Mono<E> getOf(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6) {
        return getByMap(map -> map.put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5).put(k6, v6));
    }

    @Override
    public Mono<E> getOf(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6, SFunction<E, Object> k7, Object v7) {
        return getByMap(map -> map.put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5).put(k6, v6).put(k7, v7));
    }

    @Override
    public Mono<E> getOf(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6, SFunction<E, Object> k7, Object v7, SFunction<E, Object> k8, Object v8) {
        return getByMap(map -> map.put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5).put(k6, v6).put(k7, v7).put(k8, v8));
    }

    @Override
    public Mono<E> getOf(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6, SFunction<E, Object> k7, Object v7, SFunction<E, Object> k8, Object v8, SFunction<E, Object> k9, Object v9) {
        return getByMap(map -> map.put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5).put(k6, v6).put(k7, v7).put(k8, v8).put(k9, v9));
    }

    @Override
    public Mono<E> getOf(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6, SFunction<E, Object> k7, Object v7, SFunction<E, Object> k8, Object v8, SFunction<E, Object> k9, Object v9, SFunction<E, Object> k10, Object v10) {
        return getByMap(map -> map.put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5).put(k6, v6).put(k7, v7).put(k8, v8).put(k9, v9).put(k10, v10));
    }

    @Override
    public Mono<E> getByIdOrFail(Serializable id) {
        return checkGetFail(getById(id));
    }

    @Override
    public Mono<E> getByQueryOrFail(Consumer<LambdaQueryWrapper<E>> consumer) {
        return checkGetFail(getByQuery(consumer));
    }

    @Override
    public Mono<E> getByMapOrFail(Consumer<QueryMap<SFunction<E, Object>, Object>> consumer) {
        return checkGetFail(getByMap(consumer));
    }

    @Override
    public Mono<E> getOfOrFail(SFunction<E, Object> k1, Object v1) {
        return checkGetFail(getOf(k1, v1));
    }

    @Override
    public Mono<E> getOfOrFail(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2) {
        return checkGetFail(getOf(k1, v1, k2, v2));
    }

    @Override
    public Mono<E> getOfOrFail(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3) {
        return checkGetFail(getOf(k1, v1, k2, v2, k3, v3));
    }

    @Override
    public Mono<E> getOfOrFail(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4) {
        return checkGetFail(getOf(k1, v1, k2, v2, k3, v3, k4, v4));
    }

    @Override
    public Mono<E> getOfOrFail(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5) {
        return checkGetFail(getOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
    }

    @Override
    public Mono<E> getOfOrFail(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6) {
        return checkGetFail(getOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6));
    }

    @Override
    public Mono<E> getOfOrFail(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6, SFunction<E, Object> k7, Object v7) {
        return checkGetFail(getOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7));
    }

    @Override
    public Mono<E> getOfOrFail(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6, SFunction<E, Object> k7, Object v7, SFunction<E, Object> k8, Object v8) {
        return checkGetFail(getOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8));
    }

    @Override
    public Mono<E> getOfOrFail(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6, SFunction<E, Object> k7, Object v7, SFunction<E, Object> k8, Object v8, SFunction<E, Object> k9, Object v9) {
        return checkGetFail(getOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9));
    }

    @Override
    public Mono<E> getOfOrFail(SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6, SFunction<E, Object> k7, Object v7, SFunction<E, Object> k8, Object v8, SFunction<E, Object> k9, Object v9, SFunction<E, Object> k10, Object v10) {
        return checkGetFail(getOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10));
    }

    @Override
    public Mono<Boolean> exists(Serializable id) {
        return checkExists(getById(id));
    }

    @Override
    public Mono<Boolean> existsByQuery(Consumer<LambdaQueryWrapper<E>> consumer) {
        return checkExists(getByQuery(consumer));
    }

    @Override
    public Mono<Boolean> existsByMap(Consumer<QueryMap<SFunction<E, Object>, Object>> consumer) {
        return checkExists(getByMap(consumer));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<Serializable> save(E entity) {
        return Mono.just(entity).flatMap(e -> {
            String keyProperty = tableInfo().getKeyProperty();
            Object keyValue = ReflectionKit.getFieldValue(e, keyProperty);
            if (keyValue == null || keyValue.toString().length() <= 0) {
                Map<String, Field> fieldMap = ReflectionKit.getFieldMap(e.getClass());
                Field field = fieldMap.get(keyProperty);
                TableId tableId = field.getDeclaredAnnotation(TableId.class);
                if (tableId != null && tableId.type() == IdType.AUTO) {
                    keyValue = idGenerator.id();
                } else {
                    keyValue = idGenerator.uuid();
                }
                field.setAccessible(true);
                try {
                    field.set(e, keyValue);
                } catch (IllegalAccessException ex) {
                    return Mono.error(ex);
                }
            }
            return Mono.fromCallable(() -> dmlRowToBoolean(mapper.insert(e)).flatMap(b -> {
                if (!b) {
                    return Mono.error(new FailedToResourceSaveException());
                }
                return Mono.just(true);
            })).subscribeOn(Schedulers.elastic())
                    .thenReturn((Serializable) keyValue);
        });
    }

    @Override
    public Mono<Serializable> saveMap(Map<String, Object> map) {
        return mapConvertToEntity(map).flatMap(this::save);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<Void> saveBatch(List<E> entityList) {
        return Flux.fromIterable(entityList).flatMap(this::save).then(Mono.empty());
    }

    @Override
    public Mono<Serializable> saveOrUpdate(E entity) {
        return Mono.just(entity).flatMap(e -> {
            String keyProperty = tableInfo().getKeyProperty();
            Object keyValue = ReflectionKit.getFieldValue(entity, keyProperty);
            if (keyValue == null || keyValue.toString().length() <= 0) {
                return save(e);
            }
            return exists(keyProperty).flatMap(bool -> {
                if (bool) {
                    return update(e);
                } else {
                    return save(e);
                }
            });
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<Boolean> update(E entity) {
        return Mono.just(entity).publishOn(Schedulers.elastic())
                .map(e -> mapper.updateById(entity))
                .flatMap(this::dmlRowToBoolean)
                .onErrorResume(t -> Mono.error(new FailedToResourceUpdateException(t.getMessage())));
    }

    @Override
    public Mono<Void> updateBatch(String ids, Map<String, Object> map) {
        return updateBatch(Arrays.asList(ids.split("[,]")), map);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<Void> updateBatch(List<? extends Serializable> idList, Map<String, Object> map) {
        return Flux.fromIterable(idList)
                .publishOn(Schedulers.elastic())
                .flatMap(id -> updateById(id, map))
                .onErrorResume(t -> Mono.error(new FailedToResourceRemoveException(t.getMessage())))
                .then(Mono.empty());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<Boolean> updateByQuery(E entity, Consumer<LambdaQueryWrapper<E>> consumer) {
        return Mono.just(consumer)
                .map(this::acceptQueryConsumer)
                .publishOn(Schedulers.elastic())
                .map(eqw -> mapper.update(entity, eqw))
                .flatMap(this::dmlRowToBoolean)
                .onErrorResume(t -> Mono.error(new FailedToResourceUpdateException(t.getMessage())));
    }

    @Override
    public Mono<Boolean> updateById(Serializable id, E entity) {
        return getByIdOrFail(id).flatMap(ignoreEntity -> {
            String keyProperty = tableInfo().getKeyProperty();
            Field keyField = ClazzUtils.findField(entityClazz(), keyProperty);
            if (keyField == null) {
                return Mono.error(new FailedToResourceSaveException("Key property (%s) not found!", keyProperty));
            }
            keyField.trySetAccessible();
            try {
                keyField.set(entity, id);
            } catch (IllegalAccessException e) {
                if (log.isDebugEnabled()) {
                    e.printStackTrace();
                }
                return Mono.error(new FailedToResourceSaveException(e.getMessage()));
            }
            return update(entity);
        });
    }

    @Override
    public Mono<Boolean> updateById(Serializable id, Map<String, Object> map) {
        return Mono.just(tableInfo().getKeyProperty())
                .flatMap(keyProperty -> Mono.just(map)
                        .flatMap(d -> {
                            d.put(keyProperty, id);
                            return Mono.just(d);
                        }))
                .flatMap(this::mapConvertToEntity)
                .flatMap(this::update);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<Boolean> updateById(Serializable id, Consumer<LambdaUpdateWrapper<E>> consumer) {
        return getByIdOrFail(id).flatMap(e -> Mono.just(consumer)
                .map(this::acceptUpdateConsumer)
                .publishOn(Schedulers.elastic())
                .map(euw -> mapper.update(e, euw))
                .flatMap(this::dmlRowToBoolean))
                .onErrorResume(t -> Mono.error(new FailedToResourceUpdateException(t.getMessage())));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<Boolean> updateByIdOfMap(Serializable id, Consumer<QueryMap<SFunction<E, Object>, Object>> consumer) {
        return getByIdOrFail(id).flatMap(e -> Mono.just(consumer)
                .map(this::acceptUpdateMapConsumer)
                .publishOn(Schedulers.elastic())
                .map(euw -> mapper.update(e, euw))
                .flatMap(this::dmlRowToBoolean))
                .onErrorResume(t -> Mono.error(new FailedToResourceUpdateException(t.getMessage())));
    }

    @Override
    public Mono<Boolean> updateOf(Serializable id, SFunction<E, Object> k1, Object v1) {
        return updateByIdOfMap(id, m -> m.put(k1, v1));
    }

    @Override
    public Mono<Boolean> updateOf(Serializable id, SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2) {
        return updateByIdOfMap(id, m -> m.put(k1, v1).put(k2, v2));
    }

    @Override
    public Mono<Boolean> updateOf(Serializable id, SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3) {
        return updateByIdOfMap(id, m -> m.put(k1, v1).put(k2, v2).put(k3, v3));
    }

    @Override
    public Mono<Boolean> updateOf(Serializable id, SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4) {
        return updateByIdOfMap(id, m -> m.put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4));
    }

    @Override
    public Mono<Boolean> updateOf(Serializable id, SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5) {
        return updateByIdOfMap(id, m -> m.put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5));
    }

    @Override
    public Mono<Boolean> updateOf(Serializable id, SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6) {
        return updateByIdOfMap(id, m -> m.put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5).put(k6, v6));
    }

    @Override
    public Mono<Boolean> updateOf(Serializable id, SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6, SFunction<E, Object> k7, Object v7) {
        return updateByIdOfMap(id, m -> m.put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5).put(k6, v6).put(k7, v7));
    }

    @Override
    public Mono<Boolean> updateOf(Serializable id, SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6, SFunction<E, Object> k7, Object v7, SFunction<E, Object> k8, Object v8) {
        return updateByIdOfMap(id, m -> m.put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5).put(k6, v6).put(k7, v7).put(k8, v8));
    }

    @Override
    public Mono<Boolean> updateOf(Serializable id, SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6, SFunction<E, Object> k7, Object v7, SFunction<E, Object> k8, Object v8, SFunction<E, Object> k9, Object v9) {
        return updateByIdOfMap(id, m -> m.put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5).put(k6, v6).put(k7, v7).put(k8, v8).put(k9, v9));
    }

    @Override
    public Mono<Boolean> updateOf(Serializable id, SFunction<E, Object> k1, Object v1, SFunction<E, Object> k2, Object v2, SFunction<E, Object> k3, Object v3, SFunction<E, Object> k4, Object v4, SFunction<E, Object> k5, Object v5, SFunction<E, Object> k6, Object v6, SFunction<E, Object> k7, Object v7, SFunction<E, Object> k8, Object v8, SFunction<E, Object> k9, Object v9, SFunction<E, Object> k10, Object v10) {
        return updateByIdOfMap(id, m -> m.put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5).put(k6, v6).put(k7, v7).put(k8, v8).put(k9, v9).put(k10, v10));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<Void> removeById(Serializable id) {
        return Mono.fromCallable(() -> mapper.deleteById(id))
                .subscribeOn(Schedulers.elastic())
                .onErrorResume(t -> Mono.error(new FailedToResourceRemoveException(t.getMessage())))
                .then(Mono.empty());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<Void> removeByQuery(Consumer<LambdaQueryWrapper<E>> consumer) {
        return Mono.just(consumer)
                .map(this::acceptQueryConsumer)
                .publishOn(Schedulers.elastic())
                .map(eqw -> mapper.delete(eqw))
                .onErrorResume(t -> Mono.error(new FailedToResourceRemoveException(t.getMessage())))
                .then(Mono.empty());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<Void> removeBatch(String ids) {
        return removeBatch(Arrays.asList(ids.split("[,]")));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<Void> removeBatch(List<? extends Serializable> idList) {
        return Flux.fromIterable(idList)
                .publishOn(Schedulers.elastic())
                .map(id -> mapper.deleteById(id))
                .onErrorResume(t -> Mono.error(new FailedToResourceRemoveException(t.getMessage())))
                .then(Mono.empty());
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<Integer> countAll() {
        return Mono.just(Wrappers.lambdaQuery(entityClazz()))
                .map(eqw -> mapper.selectCount(eqw))
                .onErrorResume(t -> Mono.error(new FailedToResourceQueryException(t.getMessage())))
                .subscribeOn(Schedulers.elastic());
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<Integer> countByQuery(Consumer<LambdaQueryWrapper<E>> consumer) {
        return Mono.just(consumer)
                .map(this::acceptQueryConsumer)
                .publishOn(Schedulers.elastic())
                .map(eqw -> mapper.selectCount(eqw))
                .onErrorResume(t -> Mono.error(new FailedToResourceQueryException(t.getMessage())));
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<Integer> countByMap(Consumer<QueryMap<SFunction<E, Object>, Object>> consumer) {
        return Mono.just(consumer)
                .map(this::acceptQueryMapConsumer)
                .publishOn(Schedulers.elastic())
                .map(eqw -> mapper.selectCount(eqw))
                .onErrorResume(t -> Mono.error(new FailedToResourceQueryException(t.getMessage())));
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<E> findAll() {
        Flux<E> push = Flux.push(fluxSink -> {
            mapper.selectListWithHandler(Wrappers.lambdaQuery(entityClazz()), ctx -> fluxSink.next(ctx.getResultObject()));
            fluxSink.complete();
        });
        return push.publishOn(Schedulers.elastic())
                .subscribeOn(Schedulers.elastic())
                .onErrorResume(t -> Mono.error(new FailedToResourceQueryException(t.getMessage())));
    }

    @Override
    public Flux<E> findByIds(Serializable... ids) {
        return findByIds(Arrays.asList(ids));
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<E> findByIds(List<? extends Serializable> ids) {
        Flux<E> push = Flux.push(fluxSink -> {
            mapper.selectBatchIdsWithHandler(ids, ctx -> fluxSink.next(ctx.getResultObject()));
            fluxSink.complete();
        });
        return push.publishOn(Schedulers.elastic())
                .subscribeOn(Schedulers.elastic())
                .onErrorResume(t -> Mono.error(new FailedToResourceQueryException(t.getMessage())));
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<E> findByQuery(Consumer<LambdaQueryWrapper<E>> consumer) {
        Flux<E> push = Flux.push(fluxSink -> {
            mapper.selectListWithHandler(acceptQueryConsumer(consumer), ctx -> fluxSink.next(ctx.getResultObject()));
            fluxSink.complete();
        });

        return push.publishOn(Schedulers.elastic())
                .subscribeOn(Schedulers.elastic())
                .onErrorResume(t -> Mono.error(new FailedToResourceQueryException(t.getMessage())));
    }

    @Override
    public Mono<PageResult<E>> page(IPage<E> page) {
        return page(page, eqw -> {
        });
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<PageResult<E>> page(IPage<E> page, Consumer<LambdaQueryWrapper<E>> consumer) {
        return Mono.just(acceptQueryConsumer(consumer))
                .publishOn(Schedulers.elastic())
                .map(eqw -> mapper.selectPage(page, eqw))
                .flatMap(result -> Mono.just(PageResult.of(
                        result.getTotal(),
                        result.getCurrent(),
                        result.getSize(),
                        result.getPages(),
                        result.getRecords())
                )).subscribeOn(Schedulers.elastic())
                .onErrorResume(t -> Mono.error(new FailedToResourceQueryException(t.getMessage())));
    }
}
