package com.nekoimi.nk.devtools.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import com.nekoimi.nk.devtools.dto.SchemaInfo;
import com.nekoimi.nk.devtools.dto.TableInfo;

import java.util.List;

/**
 * nekoimi  2022/1/9 21:30
 */
public class PostgreSQLDbInformationService extends AbstractDbInformationService {

    @Override
    public List<SchemaInfo> schemas() {
        JdbcUtils.getDbType("");
        return null;
    }

    @Override
    public List<TableInfo> allTables(String schema) {
        return null;
    }
}
