package com.tbc.ddd.infrastructure.user.repository;

import com.tbc.ddd.domain.user.model.UserInfo;
import org.springframework.stereotype.Repository;

import com.tbc.ddd.domain.south.user.repository.UserInfoRepository;
import com.tbc.ddd.domain.user.model.UserId;
import com.tbc.ddd.infrastructure.user.converter.UserInfoConverter;
import com.tbc.ddd.infrastructure.user.entity.UserInfoPO;
import com.tbc.ddd.infrastructure.user.mapper.UserInfoMapper;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户仓储服务
 *
 * @author Johnson.Jia
 * @date 2023/3/15 11:03:34
 */
@Repository
@RequiredArgsConstructor
public class UserInfoRepositoryImpl implements UserInfoRepository {

    final UserInfoMapper userInfoMapper;
    final UserInfoConverter userInfoConverter;

    @Override
    public void deleteById(UserId userId) {
        userInfoMapper.deleteById(userId.getId());
    }

    @Override
    public UserInfo getById(UserId userId) {
        UserInfoPO userInfoPO = userInfoMapper.selectById(userId.getId());
        return userInfoConverter.toUserInfo(userInfoPO);
    }

    @Override
    public UserInfo save(UserInfo userInfo) {
        UserInfoPO userInfoPO = userInfoConverter.toUserInfoPo(userInfo);
        if (userInfo.getUserId() != null) {
            userInfoMapper.updateById(userInfoPO);
        } else {
            userInfoMapper.insert(userInfoPO);
        }
        return userInfoConverter.toUserInfo(userInfoPO);
    }

    @Override
    public List<UserInfo> getListByIds(List<UserId> userIds) {
        List<UserInfoPO> list =
            userInfoMapper.selectBatchIds(userIds.stream().map(UserId::getId).collect(Collectors.toList()));
        return userInfoConverter.toUserInfoList(list);
    }
}
