package com.nekoimi.nk.framework.mybatis.enums;

/**
 * nekoimi  2022/1/25 16:04
 */
public enum ExtendSqlMethod {
    SELECT_LIST_WITH_HANDLER("selectListWithHandler", "查询满足条件所有数据", "<script>%s SELECT %s FROM %s %s %s %s\n</script>"),
    SELECT_BATCH_BY_IDS_WITH_HANDLER("selectBatchIdsWithHandler", "根据ID集合，批量查询数据", "<script>SELECT %s FROM %s WHERE %s IN (%s) %s </script>"),
    SELECT_PAGE_WITH_HANDLER("selectPageWithHandler", "查询满足条件所有数据（并翻页）", "<script>%s SELECT %s FROM %s %s %s %s\n</script>"),
    ;

    private final String method;
    private final String desc;
    private final String sql;

    ExtendSqlMethod(String method, String desc, String sql) {
        this.method = method;
        this.desc = desc;
        this.sql = sql;
    }

    public String getMethod() {
        return method;
    }

    public String getDesc() {
        return desc;
    }

    public String getSql() {
        return sql;
    }
}
