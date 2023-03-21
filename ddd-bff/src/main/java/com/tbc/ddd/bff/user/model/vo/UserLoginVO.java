package com.tbc.ddd.bff.user.model.vo;

import com.tbc.ddd.common.bean.Secret;
import com.tbc.ddd.domain.role.dto.RoleDTO;

import lombok.Data;

/**
 * 用户登录信息
 *
 * @author Johnson.Jia
 * @date 2023/3/17 17:54:17
 */
@Data
public class UserLoginVO {
    /**
     * 手机号码
     */
    private String phone;

    /**
     * 角色
     */
    private RoleDTO role;

    /**
     * 密钥
     */
    private Secret secret;
}
