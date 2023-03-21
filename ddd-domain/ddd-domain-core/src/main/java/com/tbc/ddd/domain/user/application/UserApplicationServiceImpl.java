package com.tbc.ddd.domain.user.application;

import java.util.Objects;

import org.apache.dubbo.config.annotation.DubboService;

import com.tbc.ddd.common.tools.VerificationUtil;
import com.tbc.ddd.domain.north.user.application.UserApplicationService;
import com.tbc.ddd.domain.role.dto.RoleDTO;
import com.tbc.ddd.domain.role.model.Role;
import com.tbc.ddd.domain.south.event.DomainEventPublisher;
import com.tbc.ddd.domain.south.role.repository.MenusRepository;
import com.tbc.ddd.domain.south.role.repository.RoleRepository;
import com.tbc.ddd.domain.south.user.repository.LoginRepository;
import com.tbc.ddd.domain.south.user.repository.UserInfoRepository;
import com.tbc.ddd.domain.user.assembler.UserAssembler;
import com.tbc.ddd.domain.user.dto.LoginDTO;
import com.tbc.ddd.domain.user.dto.UserDTO;
import com.tbc.ddd.domain.user.dto.UserInfoDTO;
import com.tbc.ddd.domain.user.dto.UserRegisterDTO;
import com.tbc.ddd.domain.user.event.LoginEvent;
import com.tbc.ddd.domain.user.event.RegisterEvent;
import com.tbc.ddd.domain.user.exception.UserException;
import com.tbc.ddd.domain.user.model.Login;
import com.tbc.ddd.domain.user.model.PhoneNumber;
import com.tbc.ddd.domain.user.model.UserInfo;
import com.tbc.ddd.domain.user.service.UserDomainService;

import lombok.RequiredArgsConstructor;

/**
 * 用户服务接口实现
 *
 * @author Johnson.Jia
 * @date 2023/3/15 17:01:31
 */
@DubboService
@RequiredArgsConstructor
public class UserApplicationServiceImpl implements UserApplicationService {

    final LoginRepository loginRepository;
    final RoleRepository roleRepository;
    final MenusRepository menusRepository;
    final UserInfoRepository userInfoRepository;

    final UserDomainService userDomainService;

    final UserAssembler userAssembler;

    final DomainEventPublisher domainEventPublisher;

    @Override
    public UserDTO loginByPhone(String phone, String password) {
        // 根据手机号 查询登录信息
        Login login = loginRepository.getByPhone(PhoneNumber.builder().phone(phone).build());
        return userLogin(login, password);
    }

    @Override
    public UserDTO loginByName(String loginName, String password) {
        Login login = loginRepository.getByLoginName(loginName);
        return userLogin(login, password);
    }

    @Override
    public UserDTO userRegister(UserRegisterDTO userRegisterDTO) {
        Login login = userAssembler.toLogin(userRegisterDTO);

        VerificationUtil.isTrue(Objects.nonNull(loginRepository.getByPhone(login.getPhone())),
            new UserException("User already exists."));
        VerificationUtil.isTrue(Objects.nonNull(loginRepository.getByLoginName(login.getLoginName())),
            new UserException("User already exists."));
        login = userDomainService.userRegister(login, userRegisterDTO.getCode());
        UserDTO userDTO = this.initUserDTO(login);
        // 发布注册成功事件
        domainEventPublisher.publishEvent(new RegisterEvent(userDTO));
        return userDTO;
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
        login.checkPassword(password);
        login.createSecret();
        userDomainService.userLogin(login);
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
        LoginDTO loginDTO = userAssembler.toLoginDto(login);
        RoleDTO roleDTO = userAssembler.toRoleDto(role);

        UserInfoDTO userInfoDTO = userAssembler.toUserInfoDto(userInfoRepository.getById(login.getUserId()));

        return UserDTO.builder().login(loginDTO).role(roleDTO).userInfo(userInfoDTO).build();
    }
}
