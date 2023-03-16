package com.tbc.ddd.infrastructure.user.converter;

import com.tbc.ddd.domain.user.model.LoginDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tbc.ddd.infrastructure.user.entity.LoginPO;

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
     * @param loginDO
     * @return
     */
    @Mapping(source = "roleId.id", target = "roleId")
    @Mapping(source = "userId.id", target = "userId")
    @Mapping(source = "phone.phone", target = "phone")
    LoginPO toLoginPo(LoginDO loginDO);

    /**
     * PO 转 DO
     *
     * @author Johnson.Jia
     * @date 2023/3/15 15:11:50
     * @param loginPO
     * @return
     */
    @Mapping(source = "roleId", target = "roleId.id")
    @Mapping(source = "userId", target = "userId.id")
    @Mapping(source = "phone", target = "phone.phone")
    LoginDO toLoginDo(LoginPO loginPO);

}
