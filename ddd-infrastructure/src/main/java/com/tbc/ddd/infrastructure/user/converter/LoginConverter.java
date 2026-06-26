package com.tbc.ddd.infrastructure.user.converter;

import com.tbc.ddd.domain.role.valueobject.RoleId;
import com.tbc.ddd.domain.user.enums.AuthTypeEnum;
import com.tbc.ddd.domain.user.model.Login;
import com.tbc.ddd.domain.user.valueobject.Phone;
import com.tbc.ddd.domain.user.valueobject.UserId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tbc.ddd.infrastructure.user.entity.LoginPO;

import java.util.List;

/**
 * 用户登录信息 对象转换
 *
 * @author Johnson.Jia
 * @date 2023/3/15 11:46:22
 */
@Mapper(componentModel = "spring")
public interface LoginConverter {

    /**
     * do 转 po
     *
     * @author Johnson.Jia
     * @date 2023/3/15 15:11:36
     * @param login
     * @return
     */
    @Mapping(source = "roleId.id", target = "roleId")
    @Mapping(source = "userId.id", target = "userId")
    @Mapping(source = "phone.phone", target = "phone")
    LoginPO toLoginPo(Login login);

    /**
     * PO 转 Login 聚合(重建场景：密文密码原样恢复)
     *
     * @author Johnson.Jia
     * @param loginPO
     * @return
     */
    default Login toLogin(LoginPO loginPO) {
        if (loginPO == null) {
            return null;
        }
        AuthTypeEnum authType = (loginPO.getAuthType() == null || loginPO.getAuthType().isEmpty()) ? null
            : AuthTypeEnum.valueOf(loginPO.getAuthType());
        Integer rid = loginPO.getRoleId();
        RoleId roleId = RoleId.builder().id(rid != null ? rid : -1).build();
        return Login.reconstitute(UserId.builder().id(loginPO.getUserId()).build(),
            Phone.builder().phone(loginPO.getPhone()).build(), loginPO.getLoginName(), loginPO.getPassword(),
            authType, loginPO.getOpenId(), loginPO.getUnionId(), roleId, loginPO.getCreateTime());
    }

    /**
     * 集合转换
     *
     * @author Johnson.Jia
     * @date 2023/3/21 11:17:22
     * @param loginPOS
     * @return
     */
    List<Login> toLoginList(List<LoginPO> loginPOS);
}
