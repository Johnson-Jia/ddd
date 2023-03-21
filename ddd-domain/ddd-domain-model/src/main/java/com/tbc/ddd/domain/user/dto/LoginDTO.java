package com.tbc.ddd.domain.user.dto;

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
     * 用户名 / 登录名 / 真实姓名
     */
    private String loginName;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 密钥
     */
    private Secret secret;

}
