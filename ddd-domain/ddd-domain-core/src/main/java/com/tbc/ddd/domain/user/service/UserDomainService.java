package com.tbc.ddd.domain.user.service;

import com.tbc.ddd.domain.user.model.Login;

/**
 * 用户领域服务
 *
 * @author Johnson.Jia
 * @date 2023/3/17 18:12:08
 */
public interface UserDomainService {

    /**
     * 用户登录
     *
     * @author Johnson.Jia
     * @date 2023/3/17 18:13:47
     * @param login
     *            登录对象
     * @return
     */
    void userLogin(Login login);

    /**
     * 获取 微信用户信息
     *
     * @author Johnson.Jia
     * @date 2023/3/21 16:48:00
     * @param login
     *            用户登录对象
     * @param code
     *            授权code
     * @return
     */
    Login userRegister(Login login, String code);
}
