package com.tbc.ddd.domain.user.model;

import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.tbc.ddd.common.bean.Secret;
import com.tbc.ddd.common.ddd.AggregateRoot;
import com.tbc.ddd.common.tools.VerificationUtil;
import com.tbc.ddd.common.utils.EncryptUtil;
import com.tbc.ddd.domain.role.model.RoleId;
import com.tbc.ddd.domain.user.exception.PasswordException;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

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
@Setter(AccessLevel.PRIVATE)
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
    private String loginName;

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
     * 密钥
     */
    private Secret secret;

    /**
     * 创建时间
     */
    private Long createTime;

    public void createSecret() {
        Secret build =
            Secret.builder().sessionId(UUID.randomUUID().toString()).secretKey(UUID.randomUUID().toString()).build();
        this.secret = build;
    }

    public void setPassword(String password) {
        VerificationUtil.isTrue(StringUtils.isBlank(password), new PasswordException("Password is not exist."));
        // 进行MD5加密
        this.password = EncryptUtil.MD5(password);
    }

    public void checkPassword(String password) {
        VerificationUtil.isTrue(StringUtils.isBlank(password), new PasswordException("Password is not exist."));

        VerificationUtil.isFalse(Objects.equals(EncryptUtil.MD5(password), this.getPassword()),
            new PasswordException("Password Error."));
    }
}
