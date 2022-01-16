package com.nekoimi.nk.auth.test.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.nekoimi.nk.framework.mybatis.plugins.desensitize.annotations.FieldDesensitize;
import com.nekoimi.nk.framework.mybatis.plugins.desensitize.enums.DesensitizeStrategy;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * User Entity
 * <p>
 * nekoimi  2022-01-16
 * <p>
 * This code is generated using nk-devtools.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户账号信息")
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

    // 主键
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键")
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
    @FieldDesensitize(DesensitizeStrategy.CHINESE_NAME)
    @ApiModelProperty(value = "用户名")
    private String username;
    // 密码
    @TableField
    @FieldDesensitize(DesensitizeStrategy.PASSWORD)
    @ApiModelProperty(value = "密码")
    private String password;
    // 手机号
    @TableField
    @FieldDesensitize(DesensitizeStrategy.MOBILE_PHONE)
    @ApiModelProperty(value = "手机号")
    private String mobile;
    // 邮箱
    @TableField
    @FieldDesensitize(DesensitizeStrategy.EMAIL)
    @ApiModelProperty(value = "邮箱")
    private String email;
    // 租户ID
    @TableField
    @ApiModelProperty(value = "租户ID")
    private String tenantId;
    // 是否启用；1 - enable，0 - disable
    @TableField
    @ApiModelProperty(value = "是否启用；1 - enable，0 - disable")
    private Integer enable;


    /**
     * ===============================================================================
     *
     * TODO 非数据库字段
     */

}
