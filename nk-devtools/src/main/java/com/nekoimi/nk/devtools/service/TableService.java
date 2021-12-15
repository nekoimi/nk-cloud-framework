package com.nekoimi.nk.devtools.service;

import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import com.nekoimi.nk.framework.base.util.StrUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * nekoimi  2021/12/14 15:35
 */
@Service
public class TableService {
    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    /**
     * 获取当前数据库中所有的表
     * @return
     */
    public List<Map<String, Object>> getAllTables() {
        String schema = StrUtils.parseSchema(datasourceUrl);
        String sql = "select TABLE_NAME as name,TABLE_COMMENT as comment, TABLE_COLLATION as collation from information_schema.`TABLES` where TABLE_SCHEMA = '" + schema + "'";
        return SqlRunner.db().selectList(sql);
    }
}
