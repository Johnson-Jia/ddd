package com.tbc.ddd.domain.south.user.repository;

import com.tbc.ddd.common.ddd.Repository;
import com.tbc.ddd.domain.user.model.UserId;
import com.tbc.ddd.domain.user.model.UserInfo;

/**
 * 用户对象仓储
 *
 * @author Johnson.Jia
 * @date 2023/3/15 11:03:15
 */
public interface UserInfoRepository extends Repository<UserInfo, UserId> {

}
