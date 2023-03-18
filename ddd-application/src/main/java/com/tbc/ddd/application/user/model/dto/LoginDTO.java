package com.tbc.ddd.application.user.model.dto;

import com.tbc.ddd.application.role.dto.RoleDTO;

import com.tbc.ddd.common.bean.Secret;
import lombok.Builder;
import lombok.Data;

/**
 * 登录信息展示对象
 *
 * @author Johnson.Jia
 * @date 2023/3/17 14:50:15
 */
@Data
@Builder
public class LoginDTO {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 用户名 / 登录名 / 真实姓名
     */
    private String userName;

    /**
     * 角色
     */
    private RoleDTO role;

    private Secret secret;

    /**
     * 创建时间
     */
    private Long createTime;
}
