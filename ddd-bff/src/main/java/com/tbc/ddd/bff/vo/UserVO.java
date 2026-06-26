package com.tbc.ddd.bff.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户信息 VO(接口层出参)
 *
 * @author Johnson.Jia
 */
@Data
@Builder
public class UserVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private LoginVO login;
    private RoleVO role;
    private UserInfoVO userInfo;
}
