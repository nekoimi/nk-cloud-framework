package com.nekoimi.nk.framework.mybatis.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import com.nekoimi.nk.framework.mybatis.dto.TableInfor;
import com.nekoimi.nk.framework.mybatis.exception.FailedToResourceOperationException;
import com.nekoimi.nk.framework.mybatis.service.DbInforService;
import com.nekoimi.nk.framework.mybatis.utils.JdbcUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * nekoimi  2022/1/16 21:01
 */
@Service
public class DbInforServiceImpl implements DbInforService {
    private static final ConcurrentMap<DbType, DbInfor> dbInforMap = new ConcurrentHashMap<>();
    {
        dbInforMap.putIfAbsent(DbType.MYSQL, new MySQLDbInfor());
        dbInforMap.putIfAbsent(DbType.POSTGRE_SQL, new PostgreSQLDbInfor());
    }

    @Value("${spring.datasource.url}")
    private String url;

    private DbType dbType() {
        return JdbcUtils.getDbType(url);
    }

    @Override
    public String name() {
        String dbName = JdbcUtil.getDbName(url);
        Assert.notNull(dbName, "获取数据库名称失败");
        return dbName;
    }

    @Override
    public List<String> schemas() {
        return Optional.ofNullable(dbInforMap.get(dbType()))
                .orElseThrow(() -> new FailedToResourceOperationException("数据库类型暂不支持"))
                .schemas();
    }

    @Override
    public List<TableInfor> tables(String schema) {
        return Optional.ofNullable(dbInforMap.get(dbType()))
                .orElseThrow(() -> new FailedToResourceOperationException("数据库类型暂不支持"))
                .tables(schema);
    }

    public interface DbInfor {
        List<String> schemas();
        List<TableInfor> tables(String schema);
    }

    public class MySQLDbInfor implements DbInfor {
        private static final String SELECT_TABLES_SQL = "select TABLE_NAME as name,TABLE_COMMENT as comment, TABLE_COLLATION as collation from information_schema.`TABLES` where TABLE_SCHEMA = '{0}'";

        @Override
        public List<String> schemas() {
            return ListUtil.toList(JdbcUtil.getDbName(url));
        }

        @Override
        public List<TableInfor> tables(String schema) {
            return SqlRunner.db().selectList(SELECT_TABLES_SQL, schema)
                    .stream()
                    .map(Dict::new)
                    .map(dict -> new TableInfor(dict.getStr("name"), dict.getStr("comment")))
                    .collect(Collectors.toList());
        }
    }

    public class PostgreSQLDbInfor implements DbInfor {
        private static final String SELECT_SCHEMAS_SQL = "SELECT schema_name AS name FROM information_schema.schemata WHERE schema_name NOT LIKE 'pg_%' AND schema_name != 'information_schema' AND catalog_name = {0} ORDER BY schema_name";
        private static final String SELECT_TABLES_SQL = "SELECT relname as name,CAST(obj_description(relfilenode,'pg_class') AS VARCHAR) AS comment FROM pg_class WHERE  relkind = 'r' AND relname NOT LIKE 'pg_%' AND relname NOT LIKE 'sql_%'AND relname IN ( SELECT tablename FROM pg_tables WHERE schemaname = {0} ) ORDER BY relname";

        @Override
        public List<String> schemas() {
            return SqlRunner.db()
                    .selectList(SELECT_SCHEMAS_SQL, name())
                    .stream()
                    .map(Dict::new)
                    .map(dict -> dict.getStr("name"))
                    .collect(Collectors.toList());
        }

        @Override
        public List<TableInfor> tables(String schema) {
            return SqlRunner.db()
                    .selectList(SELECT_TABLES_SQL, schema)
                    .stream()
                    .map(Dict::new)
                    .map(dict -> new TableInfor(dict.getStr("name"), dict.getStr("comment")))
                    .collect(Collectors.toList());
        }
    }
}
