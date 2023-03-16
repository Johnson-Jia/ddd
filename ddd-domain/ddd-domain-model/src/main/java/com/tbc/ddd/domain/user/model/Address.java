package com.tbc.ddd.domain.user.model;

import com.tbc.ddd.common.ddd.ValueObject;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * 地址信息
 *
 * @author Johnson.Jia
 * @date 2023/3/15 17:25:22
 */
@Data
@Builder
@Setter(AccessLevel.PRIVATE)
public class Address implements ValueObject<Address>, Serializable {

    private static final long serialVersionUID = 2890852357502148172L;

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

    @Override
    public boolean sameValueAs(Address address) {
        return Objects.equals(this, address);
    }
}
