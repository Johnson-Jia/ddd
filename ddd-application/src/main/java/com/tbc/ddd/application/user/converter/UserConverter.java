package com.tbc.ddd.application.user.converter;

import com.tbc.ddd.application.role.dto.MenusDTO;
import com.tbc.ddd.application.role.dto.RoleDTO;
import com.tbc.ddd.application.user.model.dto.UserInfoDTO;
import com.tbc.ddd.application.user.model.dto.UserRegisterDTO;
import com.tbc.ddd.application.user.model.req.UserRegisterReq;
import com.tbc.ddd.domain.role.model.Menus;
import com.tbc.ddd.domain.role.model.Role;
import com.tbc.ddd.domain.user.model.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tbc.ddd.application.user.model.dto.LoginDTO;
import com.tbc.ddd.domain.user.model.Login;

/**
 * 用户登录信息 对象转换
 *
 * @author Johnson.Jia
 * @date 2023/3/15 11:46:22
 */
@Mapper(componentModel = "spring")
public interface UserConverter {

    /**
     * DO 转 DTO
     *
     * @param login
     * @return
     * @author Johnson.Jia
     * @date 2023/3/15 15:11:36
     */
    @Mapping(source = "userId.id", target = "userId")
    @Mapping(source = "phone.phone", target = "phone")
    LoginDTO toLoginDto(Login login);

    /**
     * do 转 dto
     *
     * @param role
     * @return
     * @author Johnson.Jia
     */
    @Mapping(source = "roleId.id", target = "id")
    RoleDTO toRoleDto(Role role);

    /**
     * do 转 dto
     *
     * @param menus
     * @return
     * @author Johnson.Jia
     * @date 2023/3/15 15:11:36
     */
    @Mapping(source = "parentId.id", target = "parentId")
    @Mapping(source = "menusId.id", target = "id")
    MenusDTO toMenusDto(Menus menus);

    /**
     * 用户详情转换
     *
     * @param userInfo
     * @return
     * @author Johnson.Jia
     * @date 2023/3/15 11:39:10
     */
    @Mapping(source = "userId.id", target = "userId")
    UserInfoDTO toUserInfoDto(UserInfo userInfo);

    /**
     * req 转 dto
     *
     * @param userRegisterReq
     * @return
     * @author Johnson.Jia
     */
    UserRegisterDTO toUserRegisterDto(UserRegisterReq userRegisterReq);

    /**
     * dto 转 do
     *
     * @param userRegisterDTO
     * @return
     * @author Johnson.Jia
     */
    @Mapping(source = "phone", target = "phone.phone")
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "wechatOpenId", ignore = true)
    @Mapping(target = "officialOpenId", ignore = true)
    @Mapping(target = "unionId", ignore = true)
    @Mapping(target = "roleId", ignore = true)
    @Mapping(target = "secret", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    Login toLogin(UserRegisterDTO userRegisterDTO);
}
