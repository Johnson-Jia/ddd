package com.tbc.ddd.domain.user.model;

import java.util.Objects;

import com.tbc.ddd.domain.user.exception.PasswordException;
import com.tbc.ddd.domain.role.model.Role;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import com.tbc.ddd.common.ddd.AggregateRoot;
import com.tbc.ddd.common.utils.EncryptUtil;
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
public class Login implements AggregateRoot {

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
     * 角色
     */
    private Role role;

    /**
     * 创建时间
     */
    private Long createTime;

    public void setPassword(String password) {
        // 进行MD5加密
        this.password = EncryptUtil.MD5(password);
    }

    public void checkPassword(String password) {
        if (StringUtils.isBlank(password)) {
            throw new PasswordException("Please input a password");
        }
        if (BooleanUtils.isNotTrue(Objects.equals(EncryptUtil.MD5(password), this.getPassword()))) {
            throw new PasswordException(" password error ");
        }
    }
}
