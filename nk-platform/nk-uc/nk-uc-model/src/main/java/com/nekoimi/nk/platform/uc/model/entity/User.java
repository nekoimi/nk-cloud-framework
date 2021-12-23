package com.nekoimi.nk.platform.uc.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * User Entity
 * <p>
 * nekoimi  2021-12-23
 * <p>
 * This code is generated using nk-devtools.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户信息")
@TableName(value = "ua_user", autoResultMap = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "ua_user";
    public static final String FILED_ID = "id";
    public static final String FILED_USERNAME = "username";
    public static final String FILED_PASSWORD = "password";
    public static final String FILED_MOBILE = "mobile";
    public static final String FILED_EMAIL = "email";
    public static final String FILED_TENANT_ID = "tenant_id";
    public static final String FILED_ENABLE = "enable";

    // 主键ID
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键ID")
    private String id;
    // 记录创建时间
    @OrderBy
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "记录创建时间")
    private LocalDateTime createdAt;
    // 记录更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "记录更新时间")
    private LocalDateTime updatedAt;
    // 逻辑删除字段
    @TableLogic(value = "NULL", delval = "CURRENT_TIMESTAMP()")
    @ApiModelProperty(value = "逻辑删除字段")
    private LocalDateTime deletedAt;

    // 用户名
    @TableField
    @ApiModelProperty(value = "用户名")
    private String username;
    // 密码
    @TableField
    @ApiModelProperty(value = "密码")
    private String password;
    // 手机号
    @TableField
    @ApiModelProperty(value = "手机号")
    private String mobile;
    // 邮箱
    @TableField
    @ApiModelProperty(value = "邮箱")
    private String email;
    // 租户ID
    @TableField
    @ApiModelProperty(value = "租户ID")
    private String tenantId;
    // 是否启用；1 - enable，0 - disable
    @TableField
    @ApiModelProperty(value = "是否启用；1 - enable，0 - disable")
    private Boolean enable;


    /**
     * ===============================================================================
     *
     * TODO 非数据库字段
     */

}
