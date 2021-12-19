package com.nekoimi.nk.auth.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.nekoimi.nk.framework.mybatis.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * User Entity
 *
 * nekoimi  2021-12-19
 *
 * This code is generated using nk-devtools.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(description = "用户信息", parent = BaseEntity.class)
@TableName(value = "ua_user", autoResultMap = true)
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public static final String FILED_USERNAME = "username";
    public static final String FILED_PASSWORD = "password";
    public static final String FILED_MOBILE = "mobile";
    public static final String FILED_EMAIL = "email";
    public static final String FILED_TENANT_ID = "tenant_id";
    public static final String FILED_ENABLE = "enable";

    // 用户名
    @ApiModelProperty(value = "用户名")
    @TableField
    private String username;
    // 密码
    @ApiModelProperty(value = "密码")
    @TableField
    private String password;
    // 手机号
    @ApiModelProperty(value = "手机号")
    @TableField
    private String mobile;
    // 邮箱
    @ApiModelProperty(value = "邮箱")
    @TableField
    private String email;
    // 租户ID
    @ApiModelProperty(value = "租户ID")
    @TableField
    private String tenantId;
    // 是否启用；1 - enable，0 - disable
    @ApiModelProperty(value = "是否启用；1 - enable，0 - disable")
    @TableField
    private Boolean enable;


    /**
     * ===============================================================================
     *
     * TODO 非数据库字段
     */

}
