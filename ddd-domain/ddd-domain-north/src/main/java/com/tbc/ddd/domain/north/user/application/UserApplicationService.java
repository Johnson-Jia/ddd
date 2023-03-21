package com.tbc.ddd.domain.north.user.application;

import com.tbc.ddd.domain.user.dto.LoginDTO;
import com.tbc.ddd.domain.user.dto.UserDTO;
import com.tbc.ddd.domain.user.dto.UserRegisterDTO;

/**
 * 用户服务接口
 *
 * @author Johnson.Jia
 * @date 2023/3/15 15:21:25
 */
public interface UserApplicationService {

    /**
     * 用户登录
     *
     * @author Johnson.Jia
     * @date 2023/3/19 12:34:20
     * @param phone
     *            手机号
     * @param password
     *            密码
     * @return
     */
    UserDTO loginByPhone(String phone, String password);

    /**
     * 用户登录
     *
     * @author Johnson.Jia
     * @date 2023/3/20 14:48:56
     * @param loginName
     *            用户名
     * @param password
     *            密码
     * @return
     */
    UserDTO loginByName(String loginName, String password);

    /**
     * 注册帐号
     *
     * @author Johnson.Jia
     * @date 2023/3/19 12:53:32
     * @param userRegisterDTO
     * @return
     */
    LoginDTO userRegister(UserRegisterDTO userRegisterDTO);

}
