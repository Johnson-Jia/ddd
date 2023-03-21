package com.tbc.ddd.domain.user.factory;

import com.tbc.ddd.domain.user.dto.AuthUserDTO;
import com.tbc.ddd.domain.user.enums.AuthTypeEnum;

/**
 * 注释
 *
 * @author Johnson.Jia
 * @date 2023/3/21 19:21:36
 */
public interface UserAuthService {

    /**
     * 获取授权类型
     *
     * @author Johnson.Jia
     * @date 2023/3/21 19:20:13
     * @return
     */
    AuthTypeEnum getAuthType();

    /**
     * 生成用户信息对象
     *
     * @author Johnson.Jia
     * @date 2023/3/21 18:53:25
     * @param code
     *            授权code
     * @return
     */
    AuthUserDTO getUserInfo(String code);
}
