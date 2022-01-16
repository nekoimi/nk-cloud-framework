package com.nekoimi.nk.devtools.service;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.nekoimi.nk.devtools.request.GenReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * nekoimi  2021/12/14 16:04
 */
@Slf4j
@Service
public class GenerateService {
    private final DataSourceProperties properties;
    private GlobalConfig globalConfig;
    private DataSourceConfig dataSourceConfig;
    private StrategyConfig strategyConfig;
    private PackageConfig packageConfig;
    private TemplateConfig templateConfig;
    private DefaultInjectionConfig injectionConfig;
    private Map<String, Object> customMap;
    private List<FileOutConfig> fileOutConfigList;

    public GenerateService(DataSourceProperties dataSourceProperties) {
        properties = dataSourceProperties;
        customMap = new HashMap<>();
        fileOutConfigList = new ArrayList<>();
        initialization();
    }

    public void initialization() {
        initGlobalConfig();
        initDataSourceConfig();
        initStrategyConfig();
        initPackageConfig();
        initPackageConfig();
        initTemplateConfig(true);
        initInjectionConfig();
    }

    public void generate(GenReq req) {
        if (req == null) return;
        AutoGenerator autoGenerator = new AutoGenerator();
        boolean isOnlyGenEntity = (req.getOnlyGenEntity() != null && req.getOnlyGenEntity());
        if (isOnlyGenEntity) {
            initTemplateConfig(false);
        }
        String outputDir = StringUtils.removeEnd(req.getLocation(), "/") + "/src/main/java";
        String packageName = req.getPackageName();
        // 全局配置
        globalConfig.setOutputDir(outputDir);
        globalConfig.setAuthor(req.getAuthor());
        // 添加schema
        dataSourceConfig.setSchemaName(req.getSchemaName());
        // 此处可以修改为您的表前缀
        strategyConfig.setTablePrefix(req.getTablePrefix());
        strategyConfig.setFieldPrefix(req.getFieldPrefix());
        strategyConfig.setInclude(req.getTableName());
        // 包配置
        packageConfig.setParent(packageName);
        packageConfig.setModuleName(req.getModuleName());
        // 自定义配置
        customMap.put("route", req.getRouter());
        customMap.put("feign", req.getFeign());
        // 添加自定义模板
        initCustomFileOutConfig(outputDir);
        injectionConfig.setMap(customMap);
        injectionConfig.setEntityName(req.getEntityName());
        injectionConfig.setFileOutConfigList(fileOutConfigList);
        // 执行生成
        autoGenerator.setGlobalConfig(globalConfig);
        autoGenerator.setDataSource(dataSourceConfig);
        autoGenerator.setStrategy(strategyConfig);
        autoGenerator.setPackageInfo(packageConfig);
        autoGenerator.setTemplate(templateConfig);
        autoGenerator.setCfg(injectionConfig);
        autoGenerator.execute();
    }

    protected void initGlobalConfig() {
        if (globalConfig == null) {
            globalConfig = new GlobalConfig();
        }
        globalConfig.setFileOverride(true);
        globalConfig.setOpen(false);
        globalConfig.setEnableCache(false);
        globalConfig.setKotlin(false);
        globalConfig.setSwagger2(false);
        globalConfig.setActiveRecord(false);
        globalConfig.setBaseResultMap(true);
        globalConfig.setDateType(DateType.TIME_PACK);
        globalConfig.setBaseColumnList(true);
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        // globalConfig.setEntityName("%s");
        globalConfig.setMapperName("%sMapper");
        globalConfig.setXmlName("%sMapper");
        globalConfig.setServiceName("%sService");
        globalConfig.setServiceImplName("%sServiceImpl");
        globalConfig.setControllerName("%sController");
    }

    protected void initDataSourceConfig() {
        if (dataSourceConfig == null) {
            dataSourceConfig = new DataSourceConfig();
        }
        dataSourceConfig.setDriverName(properties.getDriverClassName());
        dataSourceConfig.setUrl(properties.getUrl());
        dataSourceConfig.setUsername(properties.getUsername());
        dataSourceConfig.setPassword(properties.getPassword());
        dataSourceConfig.setTypeConvert(new MySqlTypeConvert() {
            // 自定义数据库表字段类型转换【可选】
            @Override
            public DbColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
//                //将数据库中datetime转换成date
//                if (fieldType.toLowerCase().contains("datetime")) {
//                    return DbColumnType.DATE;
//                }
                return (DbColumnType) super.processTypeConvert(globalConfig, fieldType);
            }
        });
    }

    protected void initStrategyConfig() {
        if (strategyConfig == null) {
            strategyConfig = new StrategyConfig();
        }
        strategyConfig.setCapitalMode(false);
        strategyConfig.setSkipView(false);
//        strategy.setNameConvert(new INameConvert() {
//            @Override
//            public String entityNameConvert(TableInfo tableInfo) {
//                return null;
//            }
//
//            @Override
//            public String propertyNameConvert(TableField field) {
//                return null;
//            }
//        });

        strategyConfig.setEntitySerialVersionUID(true);
        strategyConfig.setEntityColumnConstant(true);
        // Builder 模式
        strategyConfig.setChainModel(true);
        strategyConfig.setEntityLombokModel(true);
        strategyConfig.setEntityBooleanColumnRemoveIsPrefix(true);
        strategyConfig.setRestControllerStyle(true);
        strategyConfig.setEntityTableFieldAnnotationEnable(true);
        // 表名生成策略
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        // 字段生成策略
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
        // 父类公共字段
        strategyConfig.setSuperEntityColumns("created_at", "updated_at", "deleted_at");
        // controller 父类
//        strategyConfig.setSuperControllerClass("com.nekoimi.nk.framework.mybatis.controller.BaseController");
        customMap.put("jsonResp", "JsonResp");
        customMap.put("jsonRespClass", "com.nekoimi.nk.framework.core.protocol.JsonResp");
        // mapper 父类
        strategyConfig.setSuperMapperClass("com.nekoimi.nk.framework.mybatis.mapper.BaseMapper");
        // 实体父类
//        strategyConfig.setSuperEntityClass(com.nekoimi.nk.framework.mybatis.entity.BaseEntity.class);
//        customMap.put("superEntityClassPackage", "com.nekoimi.nk.framework.mybatis.entity");
        // 接口父类
        strategyConfig.setSuperServiceClass(com.nekoimi.nk.framework.mybatis.service.ReactiveCrudService.class);
        // 接口实现类父类
        strategyConfig.setSuperServiceImplClass(com.nekoimi.nk.framework.mybatis.service.impl.ReactiveCrudServiceImpl.class);
    }

    protected void initPackageConfig() {
        if (packageConfig == null) {
            packageConfig = new PackageConfig();
        }
        packageConfig.setEntity("entity");
        packageConfig.setController("controller");
        packageConfig.setMapper("mapper");
        packageConfig.setXml("mapper");
        packageConfig.setService("service");
        packageConfig.setServiceImpl("service.impl");
    }

    protected void initTemplateConfig(boolean gen) {
        if (templateConfig == null) {
            templateConfig = new TemplateConfig();
        }
        if (gen) {
            templateConfig.setController("templates/controller.java.vm");
            templateConfig.setEntity("templates/entity.java.vm");
            templateConfig.setMapper("templates/mapper.java.vm");
            templateConfig.setXml("templates/mapper.xml.vm");
            templateConfig.setService("templates/service.java.vm");
            templateConfig.setServiceImpl("templates/serviceimpl.java.vm");
        } else {
            templateConfig.setController(null);
//            templateConfig.setEntity(null);
//            templateConfig.setMapper(null);
//            templateConfig.setXml(null);
            templateConfig.setService(null);
            templateConfig.setServiceImpl(null);
        }
    }

    protected void initInjectionConfig() {
        if (injectionConfig == null) {
            injectionConfig = new DefaultInjectionConfig();
        }
    }

    protected void initCustomFileOutConfig(String outputDir) {
        customMap.put("feignClientPackage", packageConfig.getParent() + ".client");
        String feignClientVmPath = "templates/feignclient.java.vm";
//        fileOutConfigList.add(new FileOutConfig(feignClientVmPath) {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                customMap.put("feignClient", tableInfo.getEntityName() + "Client");
//                customMap.put("feignClientFallback", tableInfo.getEntityName() + "ClientFallback");
//                return Paths.get(outputDir, packageConfig.getParent().replace(".", File.separator),
//                        "client", tableInfo.getEntityName() + "Client.java")
//                        .toAbsolutePath()
//                        .toString();
//            }
//        });

        String feignClientFallbackVmPath = "templates/feignclient.fallback.java.vm";
//        fileOutConfigList.add(new FileOutConfig(feignClientFallbackVmPath) {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                return Paths.get(outputDir, packageConfig.getParent().replace(".", File.separator),
//                        "client", tableInfo.getEntityName() + "ClientFallback.java")
//                        .toAbsolutePath()
//                        .toString();
//            }
//        });

        String originalControllerVmPath = "templates/controller.original.java.vm";
//        fileOutConfigList.add(new FileOutConfig(originalControllerVmPath) {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                customMap.put("originalControllerName", tableInfo.getEntityName() + "OriginalController");
//                return Paths.get(outputDir, packageConfig.getParent().replace(".", File.separator),
//                        "controller", tableInfo.getEntityName() + "OriginalController.java")
//                        .toAbsolutePath()
//                        .toString();
//            }
//        });
    }
}
