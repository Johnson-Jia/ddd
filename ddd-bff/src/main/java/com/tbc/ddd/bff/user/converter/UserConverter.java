package com.tbc.ddd.bff.user.converter;

import org.mapstruct.Mapper;

import com.tbc.ddd.bff.user.command.UserRegisterCommand;
import com.tbc.ddd.bff.vo.LoginVO;
import com.tbc.ddd.bff.vo.MenusVO;
import com.tbc.ddd.bff.vo.RoleVO;
import com.tbc.ddd.bff.vo.UserInfoVO;
import com.tbc.ddd.bff.vo.UserVO;
import com.tbc.ddd.domain.north.role.dto.MenusDTO;
import com.tbc.ddd.domain.north.role.dto.RoleDTO;
import com.tbc.ddd.domain.north.user.dto.LoginDTO;
import com.tbc.ddd.domain.north.user.dto.UserDTO;
import com.tbc.ddd.domain.north.user.dto.UserInfoDTO;
import com.tbc.ddd.domain.north.user.dto.UserRegisterDTO;

/**
 * bff 转换器：command→DTO、DTO→VO
 *
 * @author Johnson.Jia
 * @date 2023/3/15 11:46:22
 */
@Mapper(componentModel = "spring")
public interface UserConverter {

    /** req 转 dto */
    UserRegisterDTO toUserRegisterDto(UserRegisterCommand userRegisterCommand);

    /** DTO 转 VO（mapstruct 自动嵌套，list/树递归） */
    UserVO toUserVO(UserDTO userDTO);

    LoginVO toLoginVO(LoginDTO loginDTO);

    RoleVO toRoleVO(RoleDTO roleDTO);

    UserInfoVO toUserInfoVO(UserInfoDTO userInfoDTO);

    MenusVO toMenusVO(MenusDTO menusDTO);

}
