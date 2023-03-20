package com.tbc.ddd.application.user.service.impl;

import java.util.Objects;

import com.tbc.ddd.application.role.dto.RoleDTO;
import com.tbc.ddd.application.user.converter.UserConverter;
import com.tbc.ddd.application.user.model.dto.UserDTO;
import com.tbc.ddd.application.user.model.dto.UserInfoDTO;
import com.tbc.ddd.application.user.model.dto.UserRegisterDTO;
import com.tbc.ddd.common.tools.VerificationUtil;
import com.tbc.ddd.domain.event.DomainEventPublisher;
import com.tbc.ddd.domain.south.user.repository.UserInfoRepository;
import com.tbc.ddd.domain.user.event.LoginEvent;
import org.springframework.stereotype.Service;

import com.tbc.ddd.application.user.model.dto.LoginDTO;
import com.tbc.ddd.application.user.service.UserApplicationService;
import com.tbc.ddd.domain.north.role.service.RoleDomainService;
import com.tbc.ddd.domain.north.user.service.UserDomainService;
import com.tbc.ddd.domain.role.model.Role;
import com.tbc.ddd.domain.south.role.repository.MenusRepository;
import com.tbc.ddd.domain.south.role.repository.RoleRepository;
import com.tbc.ddd.domain.south.user.repository.LoginRepository;
import com.tbc.ddd.domain.user.model.Login;
import com.tbc.ddd.domain.user.model.Phone;

import lombok.RequiredArgsConstructor;

/**
 * 用户服务接口
 *
 * @author Johnson.Jia
 * @date 2023/3/15 17:01:31
 */
@Service
@RequiredArgsConstructor
public class UserApplicationServiceImpl implements UserApplicationService {

    final LoginRepository loginRepository;
    final RoleRepository roleRepository;
    final MenusRepository menusRepository;
    final UserInfoRepository userInfoRepository;

    final RoleDomainService roleDomainService;
    final UserDomainService userDomainService;

    final UserConverter userConverter;

    final DomainEventPublisher domainEventPublisher;

    @Override
    public UserDTO loginByPhone(String phone, String password) {
        // 根据手机号 查询登录信息
        Login login = loginRepository.getByPhone(Phone.builder().phone(phone).build());
        return userLogin(login, password);
    }

    @Override
    public UserDTO loginByName(String loginName, String password) {
        Login login = loginRepository.getByLoginName(loginName);
        return userLogin(login, password);
    }

    @Override
    public LoginDTO userRegister(UserRegisterDTO userRegisterDTO) {
        Login login = userConverter.toLogin(userRegisterDTO);
        login.setPassword(login.getPassword());
        return null;
    }

    /**
     * 用户登录
     *
     * @author Johnson.Jia
     * @date 2023/3/20 14:53:33
     * @param login
     * @param password
     * @return
     */
    private UserDTO userLogin(Login login, String password) {
        VerificationUtil.isTrue(Objects.isNull(login), "用户不存在");
        // 校验密码并生成session
        userDomainService.userLogin(login, password);
        UserDTO user = this.initUserDTO(login);
        // 发布登录成功事件
        domainEventPublisher.publishEvent(new LoginEvent(login));
        return user;
    }

    /**
     * 初始化 userDTO
     */
    private UserDTO initUserDTO(Login login) {
        // 获取角色信息
        Role role = roleRepository.getById(login.getRoleId());
        if (role != null) {
            // 获取角色菜单列表 ，生成菜单
            role.createMenusTree(menusRepository.getListByIds(role.getMenus()));
        }
        LoginDTO loginDTO = userConverter.toLoginDto(login);
        RoleDTO roleDTO = userConverter.toRoleDto(role);

        UserInfoDTO userInfoDTO = userConverter.toUserInfoDto(userInfoRepository.getById(login.getUserId()));

        return UserDTO.builder().login(loginDTO).role(roleDTO).userInfo(userInfoDTO).build();
    }
}
