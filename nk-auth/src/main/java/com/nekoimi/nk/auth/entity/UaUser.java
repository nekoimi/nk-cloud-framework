package com.nekoimi.nk.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.nekoimi.nk.framework.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * nekoimi  2021/12/17 22:55
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName(value = "ua_user", autoResultMap = true)
public class UaUser extends BaseEntity {
    private String username;
}
