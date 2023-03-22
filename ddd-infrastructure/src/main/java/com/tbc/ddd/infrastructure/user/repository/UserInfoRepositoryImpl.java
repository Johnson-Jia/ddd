package com.tbc.ddd.infrastructure.user.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.tbc.ddd.domain.south.user.repository.UserInfoRepository;
import com.tbc.ddd.domain.user.valueobject.UserId;
import com.tbc.ddd.domain.user.model.UserInfo;
import com.tbc.ddd.infrastructure.user.converter.UserInfoConverter;
import com.tbc.ddd.infrastructure.user.entity.UserInfoPO;
import com.tbc.ddd.infrastructure.user.mapper.UserInfoMapper;

import lombok.RequiredArgsConstructor;

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
        UserInfoPO userInfoPo = userInfoMapper.selectById(userId.getId());
        return userInfoConverter.toUserInfo(userInfoPo);
    }

    @Override
    public UserInfo save(UserInfo userInfo) {
        UserInfoPO userInfoPo = userInfoConverter.toUserInfoPo(userInfo);
        userInfoPo.setCreateTime(System.currentTimeMillis());
        userInfoMapper.insert(userInfoPo);
        return userInfoConverter.toUserInfo(userInfoPo);
    }

    @Override
    public UserInfo update(UserInfo userInfo) {
        UserInfoPO userInfoPo = userInfoConverter.toUserInfoPo(userInfo);
        userInfoMapper.updateById(userInfoPo);
        return userInfoConverter.toUserInfo(userInfoPo);
    }

    @Override
    public List<UserInfo> getListByIds(List<UserId> userIds) {
        List<UserInfoPO> list =
            userInfoMapper.selectBatchIds(userIds.stream().map(UserId::getId).collect(Collectors.toList()));
        return userInfoConverter.toUserInfoList(list);
    }
}
