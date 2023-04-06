package com.tbc.ddd.domain.user.factory.auth;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.tbc.ddd.common.exception.BaseException;
import com.tbc.ddd.common.tools.VerificationUtil;
import com.tbc.ddd.domain.user.dto.AuthUserDTO;
import com.tbc.ddd.domain.user.enums.AuthTypeEnum;
import com.tbc.ddd.domain.user.enums.GenderEnum;
import com.tbc.ddd.domain.user.valueobject.Address;

/**
 * 钉钉授权实现
 *
 * @author Johnson.Jia
 * @date 2023/3/21 18:54:22
 */
@Service
public class DingDingAuthImpl implements UserAuthService {

    @Override
    public AuthTypeEnum getAuthType() {
        return AuthTypeEnum.DINDING;
    }

    @Override
    public AuthUserDTO getUserInfo(String code) {
        VerificationUtil.isTrue(StringUtils.isBlank(code), new BaseException("Code is not exist."));
        // TODO 获取钉钉 用户信息 并创建用户详情对象
        String avatarUrl =
            "https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTLrOiaEqgufwDRaMJVf9p1YgrWxYnXwNjTN7NGvEIxCibqGvSS9r0CdPnX4ZKUfUBCialibJNhloB0dQg/132";
        String nickName = "钉钉昵称";
        GenderEnum gender = GenderEnum.MAN;

        Address build = Address.builder().country("中国").province("上海").city("上海").address("宝山区xxxx小区").build();

        String replace = UUID.randomUUID().toString().replace("-", "");

        return AuthUserDTO.builder().openId(replace).unionId(replace).avatarUrl(avatarUrl).address(build)
            .nickName(nickName).gender(gender).build();
    }
}
