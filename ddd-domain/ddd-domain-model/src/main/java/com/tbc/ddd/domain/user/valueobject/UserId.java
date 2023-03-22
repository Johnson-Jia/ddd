package com.tbc.ddd.domain.user.valueobject;

import java.io.Serializable;
import java.util.Objects;

import com.tbc.ddd.common.ddd.ValueObject;

import lombok.*;

/**
 * 用户ID 值对象
 *
 * @author Johnson.Jia
 * @date 2023/3/16 17:45:38
 */
@Data
@Builder
@Setter(AccessLevel.PRIVATE)
public class UserId implements ValueObject<UserId>, Serializable {
    private static final long serialVersionUID = 9139425726717843800L;

    /**
     * 用户id
     */
    @NonNull
    private Long id;

    @Override
    public boolean sameValueAs(UserId other) {
        return Objects.equals(this, other);
    }

}
