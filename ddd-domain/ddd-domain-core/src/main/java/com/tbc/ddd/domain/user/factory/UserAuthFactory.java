package com.tbc.ddd.domain.user.factory;

import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;

import com.tbc.ddd.common.exception.BaseException;
import com.tbc.ddd.common.tools.VerificationUtil;
import com.tbc.ddd.domain.user.factory.auth.UserAuthService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.tbc.ddd.domain.user.enums.AuthTypeEnum;

import lombok.RequiredArgsConstructor;

/**
 * 用户信息工厂接口
 *
 * @author Johnson.Jia
 * @date 2023/3/21 18:45:23
 */
@Service
@RequiredArgsConstructor
public class UserAuthFactory {

    final ApplicationContext context;
    UserAuthService defaultAuthService;

    @PostConstruct
    void init() {
        defaultAuthService = createAuthService(AuthTypeEnum.UNKNOWN);
    }

    /**
     * 工厂实现创建
     *
     * @author Johnson.Jia
     * @date 2023/3/21 18:55:47
     * @param authTypeEnum
     *            授权类型
     * @return
     */
    public UserAuthService createAuthService(AuthTypeEnum authTypeEnum) {
        VerificationUtil.isTrue(Objects.isNull(authTypeEnum), new BaseException("AuthType is not exist."));

        Map<String, UserAuthService> beans = context.getBeansOfType(UserAuthService.class);
        for (UserAuthService authService : beans.values()) {
            if (authService.getAuthType() == authTypeEnum) {
                return authService;
            }
        }
        return defaultAuthService;
    }

}
