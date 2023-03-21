package com.tbc.ddd.domain.user.model;

import java.io.Serializable;
import java.util.Objects;

import com.tbc.ddd.common.ddd.ValueObject;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * 手机号码
 *
 * @author Johnson.Jia
 * @date 2023/3/16 19:08:56
 */
@Data
@Builder
public class PhoneNumber implements ValueObject<PhoneNumber>, Serializable {

    private static final long serialVersionUID = -4312753976870166528L;

    /**
     * 手机号码
     */
    @NonNull
    private String phone;

    @Override
    public boolean sameValueAs(PhoneNumber other) {
        return Objects.equals(this, other);
    }
}
