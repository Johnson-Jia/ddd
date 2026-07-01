package com.tbc.ddd.domain.user.factory;

import java.util.EnumMap;
import java.util.Map;

import jakarta.annotation.PostConstruct;

import com.tbc.ddd.common.exception.BaseException;
import com.tbc.ddd.common.tools.VerificationUtil;
import com.tbc.ddd.domain.user.enums.AuthTypeEnum;
import com.tbc.ddd.domain.user.factory.auth.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * 用户授权服务工厂
 *
 * @author Johnson.Jia
 * @date 2023/3/21 18:45:23
 */
@Service
@RequiredArgsConstructor
public class UserAuthFactory {

    final ApplicationContext context;

    Map<AuthTypeEnum, UserAuthService> authServiceMap;

    @PostConstruct
    void init() {
        authServiceMap = new EnumMap<>(AuthTypeEnum.class);
        context.getBeansOfType(UserAuthService.class).values()
            .forEach(authService -> authServiceMap.put(authService.getAuthType(), authService));
    }

    /**
     * 按授权类型获取授权服务
     *
     * @author Johnson.Jia
     * @param authTypeEnum
     *            授权类型
     * @return 命中的授权服务；未命中返回 UNKNOWN 默认实现
     */
    public UserAuthService createAuthService(AuthTypeEnum authTypeEnum) {
        VerificationUtil.isTrue(authTypeEnum == null, new BaseException("AuthType is not exist."));
        return authServiceMap.getOrDefault(authTypeEnum, authServiceMap.get(AuthTypeEnum.UNKNOWN));
    }

}
