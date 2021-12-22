package com.nekoimi.nk.framework.mybatis.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * nekoimi  2021/12/18 16:29
 */
@Getter
@ApiModel(description = "分页数据结构")
@AllArgsConstructor(staticName = "of")
public class PageResult<T> {
    // 总条数
    @ApiModelProperty("总条数")
    private final long total;
    // 当前页数
    @ApiModelProperty("当前页数")
    private final long page;
    // 每页显示数量
    @ApiModelProperty("每页显示数量")
    private final long pageSize;
    // 最后一页页码
    @ApiModelProperty("最后一页页码")
    private final long lastPage;
    // 数据列表
    @ApiModelProperty("数据列表")
    private final List<T> list;
}