package com.nekoimi.nk.framework.mybatis.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import reactor.core.publisher.Flux;

/**
 * nekoimi  2021/12/18 16:29
 */
@Getter
@AllArgsConstructor(staticName = "of")
public class PageResult<T> {
    // 总条数
    private final Integer total;
    // 当前页数
    private final Integer page;
    // 每页显示数量
    private final Integer pageSize;
    // 最后一页页码
    private final Integer lastPage;
    // 数据列表
    private final Flux<T> list;
}