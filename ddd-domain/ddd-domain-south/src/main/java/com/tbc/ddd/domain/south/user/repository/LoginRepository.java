package com.tbc.ddd.domain.south.user.repository;

import com.tbc.ddd.common.ddd.Repository;
import com.tbc.ddd.domain.user.model.Login;
import com.tbc.ddd.domain.user.model.Phone;
import com.tbc.ddd.domain.user.model.UserId;

/**
 * 用户登录信息
 *
 * @author Johnson.Jia
 * @date 2023/3/15 11:45:47
 */
public interface LoginRepository extends Repository<Login, UserId> {

    /**
     * 查询登录信息
     *
     * @author Johnson.Jia
     * @date 2023/3/15 17:20:14
     * @param phone
     *            手机号码
     * @return
     */
    Login getByPhone(Phone phone);

}
