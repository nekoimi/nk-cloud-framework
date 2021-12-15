package com.nekoimi.nk.devtools.service;

import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * nekoimi  2021/12/15 11:07
 */
@Getter
@Setter
public class DefaultInjectionConfig extends InjectionConfig {
    private String entityName;
    private Map<String, Object> customMap;

    @Override
    public void initMap() {
        if (StringUtils.isNotEmpty(entityName)) {
            List<TableInfo> tableInfoList = this.getConfig().getTableInfoList();
            if (tableInfoList != null) {
                for (TableInfo tableInfo : tableInfoList) {
                    tableInfo.setEntityName(entityName);
                }
            }
        }
        this.setMap(customMap);
    }
}
