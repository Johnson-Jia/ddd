package com.tbc.ddd.domain.user.model;

import com.tbc.ddd.common.ddd.AggregateRoot;
import com.tbc.ddd.common.ddd.Entity;
import com.tbc.ddd.domain.user.enums.GenderEnum;

import lombok.Builder;
import lombok.Data;

/**
 * 用户详情
 *
 * @author Johnson.Jia
 * @since 2023-03-15
 */
@Data
@Builder
public class UserInfo implements AggregateRoot {

    /**
     * 用户id
     */
    private UserId userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户性别
     */
    private GenderEnum gender;

    /**
     * 用户头像 地址
     */
    private String avatarUrl;

    /**
     * 身份证 号码
     */
    private String identityCard;

    /**
     * 百度云人脸face token 标识
     */
    private String faceToken;

    /**
     * 人脸识别图片url列表 多张 ";" 分号分割。 上传到 阿里云oss
     */
    private String faceImageUrl;

    /**
     * 地址信息
     */
    private Address address;

    /**
     * 创建时间 时间戳
     */
    private Long createTime;
}
