package com.tbc.ddd.domain.user.model;

import java.io.Serializable;

import com.tbc.ddd.common.ddd.Entity;
import com.tbc.ddd.domain.role.model.RoleId;

import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * 用户登录信息 对象
 * </p>
 *
 * @author Johnson.Jia
 * @since 2023-03-15
 */
@Data
@Builder
public class LoginDO implements Entity, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private UserId userId;

    /**
     * 手机号码
     */
    private Phone phone;

    /**
     * 用户名 / 登录名 / 真实姓名
     */
    private String userName;

    /**
     * 用户登录密码
     */
    private String password;

    /**
     * 用户 微信 小程序唯一标识 open id
     */
    private String wechatOpenId;

    /**
     * 微信公众号 open id
     */
    private String officialOpenId;

    /**
     * 用户在微信开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回
     */
    private String unionId;

    /**
     * 角色ID
     */
    private RoleId roleId;

    /**
     * 创建时间
     */
    private Long createTime;

}
