package com.tbc.ddd.domain.user.model;

import java.io.Serializable;
import java.util.Objects;

import com.tbc.ddd.common.ddd.ValueObject;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * 用户ID 值对象
 *
 * @author Johnson.Jia
 * @date 2023/3/16 17:45:38
 */
@Data
@Builder
public class UserId implements ValueObject<UserId>, Serializable {

    private static final long serialVersionUID = 5073247377863469947L;

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
