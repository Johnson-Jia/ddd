package com.tbc.ddd.application.user.service;

import com.tbc.ddd.application.user.model.dto.LoginDTO;

/**
 * 用户服务接口
 *
 * @author Johnson.Jia
 * @date 2023/3/15 15:21:25
 */
public interface UserService {

    LoginDTO userLogin(LoginDTO LoginDTO);

}
