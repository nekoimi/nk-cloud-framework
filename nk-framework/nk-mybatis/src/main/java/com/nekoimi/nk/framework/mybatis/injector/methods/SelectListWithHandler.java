package com.nekoimi.nk.framework.mybatis.injector.methods;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.nekoimi.nk.framework.mybatis.enums.ExtendSqlMethod;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * nekoimi  2021/12/18 21:57
 *
 * 处理大数据列表
 *
 * @see com.baomidou.mybatisplus.core.enums.SqlMethod
 */
public class SelectListWithHandler extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        ExtendSqlMethod method = ExtendSqlMethod.SELECT_LIST_WITH_HANDLER;
        String sql = String.format(method.getSql(), sqlFirst(), sqlSelectColumns(tableInfo, true), tableInfo.getTableName(),
                sqlWhereEntityWrapper(true, tableInfo), sqlOrderBy(tableInfo), sqlComment());
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addSelectMappedStatementForTable(mapperClass, method.getMethod(), sqlSource, tableInfo);
    }
}
