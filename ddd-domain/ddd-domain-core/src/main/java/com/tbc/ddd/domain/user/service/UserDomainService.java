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

    Login userRegister(Login login);
}
