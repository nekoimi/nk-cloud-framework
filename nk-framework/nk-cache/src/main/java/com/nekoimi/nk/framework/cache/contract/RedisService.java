package com.nekoimi.nk.framework.cache.contract;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * nekoimi  2021/12/21 14:03
 */
public interface RedisService {
    /**
     * 删除缓存
     *
     * @param keys
     */
    Mono<Long> delete(String... keys);

    /**
     * 删除缓存
     *
     * @param keys
     */
    Mono<Long> delete(Flux<String> keys);

    /**
     * 判断缓存是否存在
     *
     * @param key
     * @return
     */
    Mono<Boolean> exists(String key);

    /**
     * 设置缓存过期时间
     *
     * @param key
     * @param seconds
     * @return
     */
    Mono<Boolean> expireSet(String key, long seconds);

    /**
     * 设置缓存过期时间
     *
     * @param key
     * @param expireAt
     * @return
     */
    Mono<Boolean> expireAt(String key, LocalDateTime expireAt);

    /**
     * 移除缓存过期时间
     *
     * @param key
     * @return
     */
    Mono<Boolean> expireRemove(String key);

    /**
     * 获取缓存过期时间
     *
     * @param key
     * @return 返回剩余秒数
     */
    Mono<Duration> expireGet(String key);

    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    Mono<Object> get(String key);

    /**
     * 获取字符串缓存
     *
     * @param key
     * @return
     */
    Mono<String> getAsString(String key);

    /**
     * 设置缓存
     *
     * @param key
     * @param value
     * @return
     */
    Mono<Boolean> set(String key, Object value);

    /**
     * 设置缓存，并设置过期时间
     *
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    Mono<Boolean> set(String key, Object value, long seconds);

    /**
     * 对指定缓存加一
     *
     * @param key
     * @return 操作之后的值
     */
    Mono<Long> increment(String key);

    /**
     * @param key
     * @param step
     * @return 操作之后的值
     */
    Mono<Long> increment(String key, long step);

    /**
     * 对指定缓存减一
     *
     * @param key
     * @return
     */
    Mono<Long> decrement(String key);

    /**
     * @param key
     * @param step
     * @return 操作之后的值
     */
    Mono<Long> decrement(String key, long step);

    /**
     * Hash(key, hashKey, value)
     * <p>
     * Hash 删除
     *
     * @param key
     * @param hashKey
     * @return
     */
    Mono<Long> hDelete(String key, Object...hashKey);

    /**
     * 判断hash中是否存在name
     *
     * @param key
     * @param hashKey
     * @return
     */
    Mono<Boolean> hExists(String key, Object hashKey);

    /**
     * 获取哈希表中的所有hashKey
     *
     * @param key
     * @return
     */
    Flux<Object> hKeys(String key);

    /**
     * 获取哈希表中所有的hashValue
     * @param key
     * @return
     */
    Flux<Object> hValues(String key);

    /**
     * 获取指定hash的全部 k => v 值
     *
     * @param key
     * @return
     */
    Flux<Map.Entry<Object, Object>> hEntries(String key);

    /**
     * 获取hash指定缓存
     *
     * @param key
     * @param hashKey
     * @return
     */
    Mono<Object> hGet(String key, Object hashKey);

    /**
     * 设置hash缓存
     *
     * @param key
     * @param hashKey
     * @param value
     * @return
     */
    Mono<Boolean> hPut(String key, Object hashKey, Object value);

    /**
     * 设置hash缓存，参数为map
     *
     * @param key
     * @param map
     * @return
     */
    Mono<Boolean> hPutAll(String key, Map<Object, Object> map);

    /**
     * 对指定缓存加一
     *
     * @param key
     * @param hashKey
     * @return
     */
    Mono<Long> hIncrement(String key, String hashKey);

    /**
     * @param key
     * @param hashKey
     * @param step
     * @return 操作之后的值
     */
    Mono<Long> hIncrement(String key, String hashKey, long step);

    /**
     * 对指定缓存减一
     *
     * @param key
     * @param hashKey
     * @return
     */
    Mono<Long> hDecrement(String key, String hashKey);

    /**
     * @param key
     * @param hashKey
     * @param step
     * @return 操作之后的值
     */
    Mono<Long> hDecrement(String key, String hashKey, long step);

    /**
     * 获取全部列表
     *
     * @param key
     * @return
     */
    Flux<Object> lGetList(String key);

    /**
     * 获取指定返回的列表
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    Flux<Object> lGetList(String key, long start, long end);

    /**
     * 获取缓存列表长度
     *
     * @param key
     * @return
     */
    Mono<Long> lSize(String key);

    /**
     * 获取列表中指定index的缓存
     *
     * @param key
     * @param index 索引 index >= 0 时， 0 首个元素，依次类推；index < 0 时，-1 最后一个元素，-2 倒数第二个元素，依次类推
     * @return
     */
    Mono<Object> lGetByIndex(String key, long index);

    /**
     * 列表后面追加
     *
     * @param key
     * @param value
     * @return
     */
    Mono<Long> lAdd(String key, Object value);

    /**
     * 列表指定位置插入
     *
     * @param key
     * @param index
     * @param value
     * @return
     */
    Mono<Boolean> lAdd(String key, long index, Object value);

    /**
     * 缓存list
     *
     * @param key
     * @param values
     * @return
     */
    Mono<Long> lAddAll(String key, List<Object> values);

    /**
     * 移除列表中所有和value值相同的缓存
     *
     * @param key
     * @param value
     * @return
     */
    Mono<Long> lDelete(String key, Object value);

    /**
     * 移除列表中和value相同的缓存
     *
     * @param key
     * @param value
     * @param count count > 0 : 从表头开始向表尾搜索，移除与 VALUE 相等的元素，数量为 COUNT
     *              count < 0 : 从表尾开始向表头搜索，移除与 VALUE 相等的元素，数量为 COUNT 的绝对值
     *              count = 0 : 移除表中所有与 VALUE 相等的值
     * @return
     */
    Mono<Long> lDelete(String key, Object value, long count);

    /**
     * 移除列表中除了 start ~ end 之间的其他数据
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    Mono<Boolean> lTrim(String key, long start, long end);

    /**
     * 获取全部set
     *
     * @param key
     * @return
     */
    Flux<Object> sGet(String key);

    /**
     * 获取set中缓存数量
     *
     * @param key
     * @return
     */
    Mono<Long> sSize(String key);

    /**
     * 判断Set中是否存在 value
     *
     * @param key
     * @param value
     * @return
     */
    Mono<Boolean> sExists(String key, Object value);

    /**
     * 缓存set
     *
     * @param key
     * @param values
     * @return
     */
    Mono<Long> sSet(String key, Object...values);

    /**
     * 删除集合中缓存
     *
     * @param key
     * @param values
     * @return
     */
    Mono<Long> sDelete(String key, Object... values);
}
