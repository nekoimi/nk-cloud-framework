package com.nekoimi.nk.framework.mybatis.page;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * nekoimi  2021/12/18 16:29
 */
@Getter
@AllArgsConstructor(staticName = "of")
public class PageResult<T> {
    // 总条数
    private final long total;
    // 当前页数
    private final long page;
    // 每页显示数量
    private final long pageSize;
    // 最后一页页码
    private final long lastPage;
    // 数据列表
    private final List<T> list;
}