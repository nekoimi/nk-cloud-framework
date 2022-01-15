package com.nekoimi.nk.devtools.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import com.nekoimi.nk.devtools.dto.SchemaInfo;
import com.nekoimi.nk.devtools.dto.TableInfo;
import com.nekoimi.nk.framework.core.utils.MapGetter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * nekoimi  2022/1/9 21:02
 */
public class MySQLDbInformationService extends AbstractDbInformationService {
    private static final String SELECT_TABLES_SQL = "select TABLE_NAME as name,TABLE_COMMENT as comment, TABLE_COLLATION as collation from information_schema.`TABLES` where TABLE_SCHEMA = '{0}'";

    @Override
    public List<SchemaInfo> schemas() {
        return Collections.singletonList(SchemaInfo.of(database()));
    }

    @Override
    public List<TableInfo> allTables(String schema) {
        return SqlRunner.db().selectList(SELECT_TABLES_SQL, schema)
                .stream().map(map -> TableInfo.of(
                        MapGetter.of(map).str("name"),
                        MapGetter.of(map).str("comment")))
                .collect(Collectors.toList());
    }
}
