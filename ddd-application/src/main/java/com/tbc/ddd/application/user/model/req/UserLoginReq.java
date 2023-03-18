package com.tbc.ddd.application.user.model.req;

import lombok.Data;

/**
 * 用户登录实体
 *
 * @author Johnson.Jia
 * @date 2023/3/17 17:48:11
 */
@Data
public class UserLoginReq {

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 登录密码
     */
    private String password;
}
