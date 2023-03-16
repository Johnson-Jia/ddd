package com.tbc.ddd.domain.user.model;

import com.tbc.ddd.common.ddd.ValueObject;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * 手机号码
 *
 * @author Johnson.Jia
 * @date 2023/3/16 19:08:56
 */
@Data
@Builder
public class Phone implements ValueObject<Phone>, Serializable {
    private static final long serialVersionUID = -5930788326255994500L;

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
