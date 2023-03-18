package com.tbc.ddd.domain.role.model;

import java.io.Serializable;
import java.util.Objects;

import com.tbc.ddd.common.ddd.ValueObject;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * 角色ID 值对象
 *
 * @author Johnson.Jia
 * @date 2023/3/16 17:53:47
 */
@Data
@Builder
public class RoleId implements ValueObject<RoleId>, Serializable {
    private static final long serialVersionUID = 7982971791598579112L;
    /**
     * 角色ID
     */
    @NonNull
    private Integer id;

    @Override
    public boolean sameValueAs(RoleId other) {
        return Objects.equals(this, other);
    }
}
