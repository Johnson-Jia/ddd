package com.tbc.ddd.domain.user.model;

import lombok.*;

/**
 * 地址信息
 *
 * @author Johnson.Jia
 * @date 2023/3/15 17:25:22
 */
@Data
@Builder
@Setter(AccessLevel.PRIVATE)
public class Address {

    /**
     * 国家
     */
    @NonNull
    private String country;

    /**
     * 用户 所在省份
     */
    @NonNull
    private String province;

    /**
     * 用户所在城市
     */
    @NonNull
    private String city;

    /**
     * 用户详细地址
     */
    @NonNull
    private String address;

}
