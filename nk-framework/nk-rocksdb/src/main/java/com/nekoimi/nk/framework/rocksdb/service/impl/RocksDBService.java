package com.nekoimi.nk.framework.rocksdb.service.impl;

import org.rocksdb.RocksDB;

/**
 * nekoimi  2022/1/6 15:57
 */
public class RocksDBService {

    static {
        RocksDB.loadLibrary();
    }
}
