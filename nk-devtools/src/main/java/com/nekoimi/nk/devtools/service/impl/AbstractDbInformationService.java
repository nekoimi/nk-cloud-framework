package com.nekoimi.nk.devtools.service.impl;

import com.nekoimi.nk.devtools.service.DbInformationService;
import com.nekoimi.nk.framework.core.utils.DatabaseUtils;
import org.springframework.beans.factory.annotation.Value;

/**
 * nekoimi  2022/1/9 21:30
 */
public abstract class AbstractDbInformationService implements DbInformationService {
    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Override
    public String database() {
        return DatabaseUtils.parseFromJdbcUrl(datasourceUrl);
    }
}
