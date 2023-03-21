package com.tbc.ddd.domain.user.factory.auth;

import org.springframework.stereotype.Service;

import com.tbc.ddd.domain.user.dto.AuthUserDTO;
import com.tbc.ddd.domain.user.enums.AuthTypeEnum;
import com.tbc.ddd.domain.user.factory.UserAuthService;

/**
 * 默认授权
 *
 * @author Johnson.Jia
 * @date 2023/3/21 19:26:14
 */
@Service
public class DefaultAuthImpl implements UserAuthService {

    @Override
    public AuthTypeEnum getAuthType() {
        return AuthTypeEnum.UNKNOWN;
    }

    @Override
    public AuthUserDTO getUserInfo(String code) {
        return AuthUserDTO.builder().build();
    }

}
