package com.tbc.ddd.infrastructure.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.*;

/**
 * <p>
 * 用户登录信息表
 * </p>
 *
 * @author Johnson.Jia
 * @since 2023-03-15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode()
@TableName("t_uc_login")
public class LoginPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 手机号码
     */
    @TableField("phone")
    private String phone;

    /**
     * 用户名 / 登录名 / 真实姓名
     */
    @TableField("login_name")
    private String loginName;

    /**
     * 用户登录密码
     */
    @TableField("password")
    private String password;

    /**
     * 用户 微信 小程序唯一标识  open id
     */
    @TableField("wechat_open_id")
    private String wechatOpenId;

    /**
     * 微信公众号 open id
     */
    @TableField("official_open_id")
    private String officialOpenId;

    /**
     * 用户在微信开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回
     */
    @TableField("union_id")
    private String unionId;

    /**
     * 角色 id
     */
    @TableField("role_id")
    private Integer roleId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;
}
