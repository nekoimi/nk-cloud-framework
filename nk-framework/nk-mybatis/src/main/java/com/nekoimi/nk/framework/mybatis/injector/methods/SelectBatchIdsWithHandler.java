package com.nekoimi.nk.framework.mybatis.injector.methods;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * nekoimi  2021/12/18 22:20
 */
public class SelectBatchIdsWithHandler extends AbstractMethod {
    private static final String METHOD_NAME = "selectBatchIdsWithHandler";

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String methodSql = "<script>SELECT %s FROM %s WHERE %s IN (%s) %s</script>";
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, String.format(methodSql,
                sqlSelectColumns(tableInfo, false), tableInfo.getTableName(), tableInfo.getKeyColumn(),
                SqlScriptUtils.convertForeach("#{item}", COLLECTION, null, "item", COMMA),
                tableInfo.getLogicDeleteSql(true, true)), Object.class);
        return addSelectMappedStatementForTable(mapperClass, METHOD_NAME, sqlSource, tableInfo);
    }
}
