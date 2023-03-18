package com.tbc.ddd.application.user.model.vo;

import com.tbc.ddd.application.role.dto.RoleDTO;
import com.tbc.ddd.common.bean.Secret;
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
