package com.nekoimi.nk.framework.base.mybatis.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * nekoimi  2021/12/6 17:29
 */
@Getter
@Setter
public class BaseEntity implements Serializable {
    @TableId(type = IdType.ASSIGN_UUID)
    protected String id;

    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime updatedAt;

    @JsonIgnore
    protected LocalDateTime deletedAt;
}
