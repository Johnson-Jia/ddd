package com.tbc.ddd.domain.user.dto;

import com.tbc.ddd.domain.user.enums.GenderEnum;
import com.tbc.ddd.domain.user.model.Address;
import lombok.Builder;
import lombok.Data;

/**
 * 授权用户信息 对象
 *
 * @author Johnson.Jia
 * @date 2023/3/21 20:17:29
 */
@Data
@Builder
public class AuthUserDTO {

    /**
     * 用户 微信 小程序唯一标识 open id
     */
    private String openId;

    /**
     * 用户在微信开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回
     */
    private String unionId;

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
     * 用户地址
     */
    private Address address;

}
