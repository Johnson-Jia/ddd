package com.tbc.ddd.domain.user.factory.auth;

import com.tbc.ddd.domain.user.enums.GenderEnum;
import com.tbc.ddd.domain.user.model.Address;
import org.springframework.stereotype.Service;

import com.tbc.ddd.domain.user.dto.AuthUserDTO;
import com.tbc.ddd.domain.user.enums.AuthTypeEnum;
import com.tbc.ddd.domain.user.factory.UserAuthService;

import java.util.UUID;

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
        String replace = UUID.randomUUID().toString().replace("-", "");
        Address build = Address.builder().country("").province("").city("").address("").build();
        return AuthUserDTO.builder().openId(replace).unionId(replace).avatarUrl("").address(build).nickName("")
            .gender(GenderEnum.MAN).build();
    }

}
