package com.tbc.ddd.bff.user.command;

import com.tbc.ddd.domain.user.enums.AuthTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户注册请求
 *
 * @author Johnson.Jia
 * @date 2023/3/19 12:30:31
 */
@Data
public class UserRegisterCommand implements Serializable {
    private static final long serialVersionUID = 8023639567059026642L;

    /**
     * 手机号
     */
    @NotBlank(message = "请输入手机号")
    private String phone;

    /**
     * 用户名 / 登录名 / 真实姓名
     */
    @NotBlank(message = "请输入登录名")
    private String loginName;

    /**
     * 登录密码
     */
    @NotBlank(message = "请输入登录密码")
    private String password;

    /**
     * 授权类型
     */
    @NotNull(message = "请输入授权类型")
    private AuthTypeEnum authType;

    /**
     * 授权code码
     */
    @NotBlank(message = "授权码不可为空")
    private String code;
}
