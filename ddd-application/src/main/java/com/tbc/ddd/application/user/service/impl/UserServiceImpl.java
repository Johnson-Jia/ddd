package com.tbc.ddd.application.user.service.impl;

import java.util.List;

import com.tbc.ddd.domain.event.DomainEventPublisher;
import com.tbc.ddd.domain.user.event.LoginSuccessEvent;
import org.springframework.stereotype.Service;

import com.tbc.ddd.application.converter.AppConverter;
import com.tbc.ddd.application.user.model.dto.LoginDTO;
import com.tbc.ddd.application.user.service.UserService;
import com.tbc.ddd.common.bean.Secret;
import com.tbc.ddd.domain.north.role.service.RoleDomainService;
import com.tbc.ddd.domain.north.user.service.UserDomainService;
import com.tbc.ddd.domain.role.model.Menus;
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
public class UserServiceImpl implements UserService {

    final LoginRepository loginRepository;
    final RoleRepository roleRepository;
    final MenusRepository menusRepository;

    final RoleDomainService roleDomainService;
    final UserDomainService userDomainService;

    final AppConverter appConverter;

    final DomainEventPublisher domainEventPublisher;

    @Override
    public LoginDTO userLogin(LoginDTO loginDTO) {
        // 根据手机号 查询登录信息
        Login login = loginRepository.getByPhone(Phone.builder().phone(loginDTO.getPhone()).build());
        // 校验密码并生成session
        Secret secret = userDomainService.userLogin(login, loginDTO.getPassword());
        // 获取角色信息
        Role role = roleRepository.getRoleById(login.getRoleId());
        // 获取角色菜单列表
        List<Menus> menus = menusRepository.getListByIds(role.getMenus());
        // 生成菜单
        role = roleDomainService.createRoleMenusTree(role, menus);
        login.setRole(role);

        loginDTO.setRole(appConverter.toRoleDto(role));
        loginDTO.setSecret(secret);
        // 发布登录成功事件
        domainEventPublisher.publishEvent(new LoginSuccessEvent(login));
        return loginDTO;
    }

}
