package com.nekoimi.nk.devtools.request;

import lombok.Getter;
import lombok.Setter;

/**
 * nekoimi  2021/12/15 9:08
 */
@Getter
@Setter
public class GenReq {
    private String location;
    private String author;
    private String packageName;
    private String moduleName;

    private String tableName;
    private String tablePrefix;
    private String fieldPrefix;
    private String entityName;
    private Boolean onlyGenEntity;

    private Router router;

    @Getter
    @Setter
    public static class Router {
        private String module;
        private String info;
        private String prefix;
        private String name;
        private String version;
    }
}
