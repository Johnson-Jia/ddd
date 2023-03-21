package com.tbc.ddd.domain.user.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 用户注册
 *
 * @author Johnson.Jia
 */
@Data
public class UserRegisterDTO implements Serializable {
    private static final long serialVersionUID = 8023639567059026642L;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 用户名 / 登录名 / 真实姓名
     */
    private String loginName;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 微信授权code码
     */
    private String wechatCode;
}
