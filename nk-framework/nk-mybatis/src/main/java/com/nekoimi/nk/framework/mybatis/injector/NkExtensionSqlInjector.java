package com.nekoimi.nk.framework.mybatis.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.nekoimi.nk.framework.mybatis.injector.methods.SelectBatchIdsWithHandler;
import com.nekoimi.nk.framework.mybatis.injector.methods.SelectListWithHandler;

import java.util.List;

/**
 * nekoimi  2021/12/18 22:03
 */
public class NkExtensionSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        methodList.add(new SelectListWithHandler());
        methodList.add(new SelectBatchIdsWithHandler());
        return methodList;
    }
}
