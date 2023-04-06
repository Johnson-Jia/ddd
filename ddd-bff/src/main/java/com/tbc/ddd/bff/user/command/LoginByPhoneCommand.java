package com.tbc.ddd.bff.user.command;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录实体
 *
 * @author Johnson.Jia
 * @date 2023/3/17 17:48:11
 */
@Data
public class LoginByPhoneCommand {

    /**
     * 手机号码
     */
    @NotBlank(message = "请输入手机号")
    private String phone;

    /**
     * 登录密码
     */
    @NotBlank(message = "请输入登录密码")
    private String password;

}
