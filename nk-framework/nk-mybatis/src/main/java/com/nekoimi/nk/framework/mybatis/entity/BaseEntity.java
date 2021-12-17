package com.nekoimi.nk.framework.mybatis.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Nekoimi  2020/5/30 23:25
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity implements Serializable {

    @TableId(type = IdType.ASSIGN_UUID)
    protected String id;

    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime updatedAt;

    @JsonIgnore
    @TableLogic(value = "NULL", delval = "CURRENT_TIMESTAMP()")
    protected LocalDateTime deletedAt;
}
