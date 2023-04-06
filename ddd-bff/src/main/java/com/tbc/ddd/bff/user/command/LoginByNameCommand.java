package com.tbc.ddd.bff.user.command;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * 用户登录实体
 *
 * @author Johnson.Jia
 * @date 2023/3/17 17:48:11
 */
@Data
public class LoginByNameCommand {

    /**
     * 用户名
     */
    @NotBlank(message = "请输入用户名")
    private String loginName;

    /**
     * 登录密码
     */
    @NotBlank(message = "请输入登录密码")
    private String password;

}
