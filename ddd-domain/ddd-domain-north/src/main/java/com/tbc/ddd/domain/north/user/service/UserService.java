package com.tbc.ddd.domain.north.user.service;

import com.tbc.ddd.domain.user.aggregate.UserAggRoot;
import com.tbc.ddd.domain.user.model.Phone;
import com.tbc.ddd.domain.user.model.UserId;

/**
 * 用户服务接口
 *
 * @author Johnson.Jia
 * @date 2023/3/15 15:21:25
 */
public interface UserService {
    /**
     * 根据用户Id 获取用户信息
     *
     * @author Johnson.Jia
     * @date 2023/3/15 17:06:42
     * @param userId
     *            用户id
     * @return
     */
    UserAggRoot getUserById(UserId userId);

    /**
     * 根据手机号获取用户信息
     *
     * @author Johnson.Jia
     * @date 2023/3/15 17:06:57
     * @param phone
     * @return
     */
    UserAggRoot getUserByPhone(Phone phone);
}
