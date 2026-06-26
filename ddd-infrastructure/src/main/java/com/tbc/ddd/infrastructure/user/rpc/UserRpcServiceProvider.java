package com.tbc.ddd.infrastructure.user.rpc;

import org.apache.dubbo.config.annotation.DubboService;

import com.tbc.ddd.domain.north.user.application.UserApplicationService;
import com.tbc.ddd.domain.north.user.dto.UserDTO;
import com.tbc.ddd.domain.north.user.dto.UserRegisterDTO;

import lombok.RequiredArgsConstructor;

/**
 * 用户应用服务 RPC 暴露适配。
 * <p>
 * 领域核心层(core)不再依赖 Dubbo；RPC 暴露(OHS 开放主机服务)下沉到基础设施层，
 * 通过委托持有 core 的 {@link UserApplicationService} 实现。
 *
 * @author Johnson.Jia
 */
@DubboService
@RequiredArgsConstructor
public class UserRpcServiceProvider implements UserApplicationService {

    private final UserApplicationService delegate;

    @Override
    public UserDTO loginByPhone(String phone, String password) {
        return delegate.loginByPhone(phone, password);
    }

    @Override
    public UserDTO loginByName(String loginName, String password) {
        return delegate.loginByName(loginName, password);
    }

    @Override
    public UserDTO userRegister(UserRegisterDTO userRegisterDTO) {
        return delegate.userRegister(userRegisterDTO);
    }
}
