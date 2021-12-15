package com.nekoimi.nk.framework.base.gen;

import com.nekoimi.nk.framework.base.exception.SystemClockErrorException;
import com.nekoimi.nk.framework.core.contract.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.lang.NonNull;

/**
 * @author Nekoimi  2020/6/1 上午9:39
 * <p>
 * 雪花算法ID生成 (每秒生成～100万不重复ID)
 * <p>
 * length: 64位
 * - 1bit => 0 (头部默认保留位)
 * - 41bit => 时间戳
 * - 5bit => dataCenterId
 * - 5bit => workId
 * - 12bit => serialNumber
 * <p>
 * 1bit - 不用                                       5bit-数据中心ID / 5bit-工作机器ID
 * ↓                                                      ↓       ↓
 * 0 - 00000000 00000000 00000000 00000000 00000000 0 - 00000 - 00000 - 00000000 0000
 * ↑                                            ↑                   ↑           ↑
 * |____________________________________________|                   |___________|
 * 41bit - 时间戳                                    12bit - 序列号
 */
@Slf4j
public class SnowflakeIdGenerator implements IdGenerator {
    // ID总长度
    private static final int ID_LENGTH = 64;
    // 头部最高位保留长度
    private static final int HEADER_LENGTH = 1;
    // 时间戳长度
    private static final int TIME_LENGTH = 41;
    // 数据中心长度
    private static final int DATA_LENGTH = 5;
    // 机器ID长度
    private static final int WORKER_LENGTH = 5;
    // 序列号长度
    private static final int SERIAL_NUMBER_LENGTH = 12;
    // 数据中心最大ID 31
    private static final int DATA_MAX_NUM = ~(-1 << DATA_LENGTH);
    // 机器最大ID 31
    private static final int WORKER_MAX_NUM = ~(-1 << WORKER_LENGTH);
    // 序列最大值 4095
    private static final int SERIAL_NUMBER_MAX_NUM = ~(-1 << SERIAL_NUMBER_LENGTH);
    // 随机获取数据中心id的参数 32
    private static final int DATA_ID_RANDOM = DATA_MAX_NUM + 1;
    // 随机获取机器id的参数 32
    private static final int WORK_ID_RANDOM = WORKER_MAX_NUM + 1;

    // 起始时间 UTC: 2020-06-01 00:00:00 / PRC: 2020-06-01 08:00:00
    private static final long START_TIME = 1590969600000L;

    // 时间戳左移位置
    private static final int LEFT_MOVE_TIME_BIT = ID_LENGTH - HEADER_LENGTH - TIME_LENGTH;
    // 数据中心id左移位数 17
    private static final int LEFT_MOVE_DATA_BIT = LEFT_MOVE_TIME_BIT - DATA_LENGTH;
    // 机器id左移位数 12
    private static final int LEFT_MOVE_WORK_BIT = LEFT_MOVE_DATA_BIT - WORKER_LENGTH;

    // 数据中心ID
    private final long DATA_CENTER_ID;
    // 机器ID
    private final long WORKER_ID;

    // 记录历史时间戳
    private static long LAST_TIME_STAMP = -1L;
    // 记录历史序列号
    private static long LAST_SERIAL_NUMBER = 0L;

    public SnowflakeIdGenerator(@NonNull long dataCenterId, @NonNull long workerId) {
        DATA_CENTER_ID = dataCenterId;
        WORKER_ID = workerId;
    }

    public synchronized Long id() {
        long now = System.currentTimeMillis();

        if (now < LAST_TIME_STAMP) {
            throw new SystemClockErrorException();
        }

        if (now == LAST_TIME_STAMP) {
            LAST_SERIAL_NUMBER = (LAST_SERIAL_NUMBER + 1) & SERIAL_NUMBER_MAX_NUM;
            if (LAST_SERIAL_NUMBER == 0) {
                now = getNextTimeMillis(LAST_TIME_STAMP);
            }
        } else {
            LAST_SERIAL_NUMBER = 0;
        }

        //上次生成ID的时间截
        LAST_TIME_STAMP = now;

        return ((now - START_TIME) << LEFT_MOVE_TIME_BIT) | (DATA_CENTER_ID << LEFT_MOVE_DATA_BIT) | (WORKER_ID << LEFT_MOVE_WORK_BIT) | LAST_SERIAL_NUMBER;
    }

    public String uuid() {
        long id = id();
        return DigestUtils.md5Hex(String.valueOf(id));
    }

    public long getNextTimeMillis(long lastTimeMillis) {
        long now = System.currentTimeMillis();
        while (now <= lastTimeMillis) {
            now = System.currentTimeMillis();
        }
        return now;
    }
}
