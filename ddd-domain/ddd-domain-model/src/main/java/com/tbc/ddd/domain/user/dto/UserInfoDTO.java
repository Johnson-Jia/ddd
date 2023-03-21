package com.tbc.ddd.domain.user.dto;

import com.tbc.ddd.domain.user.enums.GenderEnum;
import com.tbc.ddd.domain.user.model.Address;

import lombok.Builder;
import lombok.Data;

/**
 * 注释
 *
 * @author Johnson.Jia
 * @date 2023/3/17 14:57:30
 */
@Data
@Builder
public class UserInfoDTO {

    /**
     * 用户id
     */
    private Long userId;

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
     * 地址信息
     */
    private Address address;

}
