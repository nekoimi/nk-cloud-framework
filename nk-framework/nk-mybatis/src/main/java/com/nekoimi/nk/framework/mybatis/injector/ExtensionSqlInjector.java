package com.nekoimi.nk.framework.mybatis.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.nekoimi.nk.framework.mybatis.injector.methods.SelectBatchIdsWithHandler;
import com.nekoimi.nk.framework.mybatis.injector.methods.SelectListWithHandler;
import com.nekoimi.nk.framework.mybatis.injector.methods.SelectPageWithHandler;

import java.util.List;

/**
 * nekoimi  2021/12/18 22:03
 * <p>
 * 注入自定义方法
 */
public class ExtensionSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        methodList.add(new SelectListWithHandler());
        methodList.add(new SelectBatchIdsWithHandler());
//        methodList.add(new SelectPageWithHandler());
        return methodList;
    }
}
