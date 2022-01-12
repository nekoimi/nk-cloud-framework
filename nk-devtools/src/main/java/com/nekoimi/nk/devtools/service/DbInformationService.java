package com.nekoimi.nk.devtools.service;

import com.nekoimi.nk.devtools.dto.SchemaInfo;
import com.nekoimi.nk.devtools.dto.TableInfo;

import java.util.List;

/**
 * nekoimi  2021/12/14 15:35
 */
public interface DbInformationService {
    /**
     * 获取数据库名称
     * @return
     */
    String database();

    /**
     * 命名空间列表
     * @return
     */
    List<SchemaInfo> schemas();

    /**
     * 获取指定命名空间下面的全部数据表
     * @param schema
     * @return
     */
    List<TableInfo> allTables(String schema);
}
