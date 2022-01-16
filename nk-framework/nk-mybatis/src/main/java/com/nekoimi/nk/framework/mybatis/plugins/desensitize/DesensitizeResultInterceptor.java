package com.nekoimi.nk.framework.mybatis.plugins.desensitize;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.nekoimi.nk.framework.mybatis.plugins.desensitize.annotations.FieldDesensitize;
import com.nekoimi.nk.framework.mybatis.plugins.desensitize.enums.DesensitizeStrategy;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

/**
 * nekoimi  2022/1/16 20:12
 */
@Intercepts({
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {
                Statement.class
        })
})
public class DesensitizeResultInterceptor implements Interceptor {
    private static final String PROPERTY_NAME_MAPPED_STATEMENT = "mappedStatement";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // DefaultResultSetHandler resultSetHandler = (DefaultResultSetHandler) invocation.getTarget();
        MetaObject mataObject = SystemMetaObject.forObject(invocation.getTarget());
        MappedStatement mappedStatement = (MappedStatement) mataObject.getValue(PROPERTY_NAME_MAPPED_STATEMENT);
        List<ResultMap> resultMapList = mappedStatement.getResultMaps();
        if (resultMapList.isEmpty()) {
            // 忽略空返回值
            return invocation.proceed();
        }

        // 获取返回值类型
        Class<?> resultType = resultMapList.stream().findFirst().get().getType();
        if (ClassUtil.isBasicType(resultType)) {
            // 忽略基本类型
            return invocation.proceed();
        }
        List<Field> fieldList = ListUtil.of(ReflectUtil.getFields(resultType, field -> AnnotationUtil.hasAnnotation(field, FieldDesensitize.class)));
        if (fieldList.isEmpty()) {
            // 没有需要脱敏的字段，直接忽略
            return invocation.proceed();
        }

        // 先调用 DefaultResultSetHandler 处理查询原始结果
        // 我们只需要使用反射修改字段值即可
        Object proceed = invocation.proceed();
        List<?> resultList = Convert.convert(new TypeReference<>() {}, proceed);
        for (Object o : resultList) {
            for (Field field : fieldList) {
                Object fieldValue = ReflectUtil.getFieldValue(o, field);
                if (!(fieldValue instanceof String)) {
                    continue;
                }
                FieldDesensitize desensitize = AnnotationUtil.getAnnotation(field, FieldDesensitize.class);
                String desensitizeValue = doDesensitize(ObjectUtil.toString(fieldValue), desensitize.value());
                ReflectUtil.setFieldValue(o, field, desensitizeValue);
            }
        }
        return resultList;
    }

    protected String doDesensitize(String rawStr, DesensitizeStrategy strategy) {
        return DesensitizedUtil.desensitized(rawStr, DesensitizedUtil.DesensitizedType.valueOf(strategy.name()));
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }
}
