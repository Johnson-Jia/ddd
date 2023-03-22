package com.tbc.ddd.domain.user.valueobject;

import java.io.Serializable;
import java.util.Objects;

import com.tbc.ddd.common.ddd.ValueObject;

import lombok.*;

/**
 * 手机号码
 *
 * @author Johnson.Jia
 * @date 2023/3/16 19:08:56
 */
@Data
@Builder
@Setter(AccessLevel.PRIVATE)
public class Phone implements ValueObject<Phone>, Serializable {

    private static final long serialVersionUID = -4312753976870166528L;

    /**
     * 手机号码
     */
    @NonNull
    private String phone;

    @Override
    public boolean sameValueAs(Phone other) {
        return Objects.equals(this, other);
    }
}
