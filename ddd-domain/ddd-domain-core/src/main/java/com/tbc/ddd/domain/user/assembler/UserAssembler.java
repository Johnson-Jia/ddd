package com.tbc.ddd.domain.user.assembler;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tbc.ddd.domain.north.role.dto.MenusDTO;
import com.tbc.ddd.domain.north.role.dto.RoleDTO;
import com.tbc.ddd.domain.role.model.Menus;
import com.tbc.ddd.domain.role.model.Role;
import com.tbc.ddd.domain.north.user.dto.LoginDTO;
import com.tbc.ddd.domain.north.user.dto.UserInfoDTO;
import com.tbc.ddd.domain.north.user.dto.UserRegisterDTO;
import com.tbc.ddd.domain.user.model.Login;
import com.tbc.ddd.domain.user.model.UserInfo;
import com.tbc.ddd.domain.user.valueobject.Phone;

/**
 * 用户 聚合 装配
 *
 * @author Johnson.Jia
 * @date 2023/3/15 11:46:22
 */
@Mapper(componentModel = "spring")
public interface UserAssembler {

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
     * 菜单集合转 DTO 集合(供应用层组装菜单树)
     */
    List<MenusDTO> toMenusDtoList(List<Menus> menus);

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
     * dto 转 Login 聚合(注册场景：明文密码经工厂加密)
     *
     * @author Johnson.Jia
     * @param userRegisterDTO
     * @return
     */
    default Login toLogin(UserRegisterDTO userRegisterDTO) {
        return Login.register(Phone.builder().phone(userRegisterDTO.getPhone()).build(),
            userRegisterDTO.getLoginName(), userRegisterDTO.getPassword(), userRegisterDTO.getAuthType());
    }

}
