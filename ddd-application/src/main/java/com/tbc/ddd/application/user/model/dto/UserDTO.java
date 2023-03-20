package com.tbc.ddd.application.user.model.dto;

import java.io.Serializable;

import com.tbc.ddd.application.role.dto.RoleDTO;
import com.tbc.ddd.common.bean.Secret;

import lombok.Builder;
import lombok.Data;

/**
 * 用户信息
 *
 * @author Johnson.Jia
 * @date 2023/3/20 14:22:34
 */
@Data
@Builder
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 8563160793380531917L;
    /**
     * 登录信息
     */
    private LoginDTO login;

    /**
     * 角色
     */
    private RoleDTO role;

    /**
     * 用户详情
     */
    private UserInfoDTO userInfo;
}
