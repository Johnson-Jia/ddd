package com.tbc.ddd.domain.user.model;

import java.util.Objects;
import java.util.UUID;

import com.tbc.ddd.domain.user.enums.AuthTypeEnum;
import com.tbc.ddd.domain.user.exception.OpenIdException;
import com.tbc.ddd.domain.user.valueobject.Phone;
import com.tbc.ddd.domain.user.valueobject.UserId;
import org.apache.commons.lang3.StringUtils;

import com.tbc.ddd.common.bean.Secret;
import com.tbc.ddd.common.ddd.AggregateRoot;
import com.tbc.ddd.common.tools.VerificationUtil;
import com.tbc.ddd.common.utils.EncryptUtil;
import com.tbc.ddd.domain.role.valueobject.RoleId;
import com.tbc.ddd.domain.user.exception.PasswordException;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * <p>
 * 用户登录信息 对象(聚合根)
 * </p>
 * <p>
 * 创建必须走 {@link #register} / {@link #reconstitute} 静态工厂,
 * 保证密码加密等不变量在构造时即成立,避免被构造器/builder 绕过。
 *
 * @author Johnson.Jia
 * @since 2023-03-15
 */
@Data
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
     * 用户登录密码(已加密)
     */
    private String password;

    /**
     * 授权类型
     */
    private AuthTypeEnum authType;

    /**
     * 用户 微信 小程序唯一标识 open id
     */
    private String openId;

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

    private Login() {
    }

    /**
     * 注册工厂：明文密码经 MD5 加密后存入。
     *
     * @author Johnson.Jia
     * @param phone
     *            手机号
     * @param loginName
     *            登录名
     * @param rawPassword
     *            明文密码
     * @param authType
     *            授权类型
     * @return
     */
    public static Login register(Phone phone, String loginName, String rawPassword, AuthTypeEnum authType) {
        VerificationUtil.isTrue(StringUtils.isBlank(rawPassword), new PasswordException("Password is not exist."));
        Login login = new Login();
        login.phone = phone;
        login.loginName = loginName;
        login.password = EncryptUtil.MD5(rawPassword);
        login.authType = authType;
        return login;
    }

    /**
     * 重建工厂：从持久化恢复,password 为密文,原样填入不重新加密。
     *
     * @author Johnson.Jia
     * @param userId
     * @param phone
     * @param loginName
     * @param password
     *            密文密码
     * @param authType
     * @param openId
     * @param unionId
     * @param roleId
     * @param createTime
     * @return
     */
    public static Login reconstitute(UserId userId, Phone phone, String loginName, String password,
        AuthTypeEnum authType, String openId, String unionId, RoleId roleId, Long createTime) {
        Login login = new Login();
        login.userId = userId;
        login.phone = phone;
        login.loginName = loginName;
        login.password = password;
        login.authType = authType;
        login.openId = openId;
        login.unionId = unionId;
        login.roleId = roleId;
        login.createTime = createTime;
        return login;
    }

    /**
     * 绑定 openId(含非空校验)
     *
     * @author Johnson.Jia
     * @param openId
     */
    public void bindOpenId(String openId) {
        VerificationUtil.isTrue(StringUtils.isBlank(openId), new OpenIdException("OpenId is not exist, auth failed."));
        this.openId = openId;
    }

    /**
     * 绑定 unionId(含非空校验)
     *
     * @author Johnson.Jia
     * @param unionId
     */
    public void bindUnionId(String unionId) {
        VerificationUtil.isTrue(StringUtils.isBlank(unionId),
            new OpenIdException("UnionId is not exist, auth failed."));
        this.unionId = unionId;
    }

    /**
     * 生成会话密钥
     */
    public void createSecret() {
        this.secret =
            Secret.builder().sessionId(UUID.randomUUID().toString()).secretKey(UUID.randomUUID().toString()).build();
    }

    /**
     * 校验密码
     *
     * @author Johnson.Jia
     * @param rawPassword
     *            明文密码
     */
    public void checkPassword(String rawPassword) {
        VerificationUtil.isTrue(StringUtils.isBlank(rawPassword), new PasswordException("Password is not exist."));

        VerificationUtil.isFalse(Objects.equals(EncryptUtil.MD5(rawPassword), this.password),
            new PasswordException("Password Error."));
    }

}
