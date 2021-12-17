package com.nekoimi.nk.framework.web.result;

import lombok.Getter;

import java.util.List;

/**
 * nekoimi  2021/12/6 16:07
 */
@Getter
public class PageResult<T> {
    // 总条数
    private final int total;
    // 当前页数
    private final int page;
    // 每页显示数量
    private final int pageSize;
    // 最后一页页码
    private final int lastPage;
    // 数据列表
    private final List<T> list;

    public PageResult(List<T> list) {
        this.total = 0;
        this.page = 0;
        this.pageSize = 0;
        this.lastPage = 0;
        this.list = list;
    }

    public PageResult(int total, int page, int pageSize, int lastPage, List<T> list) {
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
        this.lastPage = lastPage;
        this.list = list;
    }
}
