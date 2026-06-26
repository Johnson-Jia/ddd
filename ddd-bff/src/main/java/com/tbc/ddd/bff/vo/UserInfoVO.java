package com.tbc.ddd.bff.vo;

import com.tbc.ddd.domain.user.enums.GenderEnum;
import com.tbc.ddd.domain.user.valueobject.Address;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户详情 VO
 *
 * @author Johnson.Jia
 */
@Data
@Builder
public class UserInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String nickName;
    private GenderEnum gender;
    private String avatarUrl;
    private String identityCard;
    private Address address;
}
