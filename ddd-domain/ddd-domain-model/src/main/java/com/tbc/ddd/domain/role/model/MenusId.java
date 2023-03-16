package com.tbc.ddd.domain.role.model;

import java.io.Serializable;
import java.util.Objects;

import com.tbc.ddd.common.ddd.ValueObject;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * 菜单、功能 ID
 *
 * @author Johnson.Jia
 * @date 2023/3/16 18:08:40
 */
@Data
@Builder
public class MenusId implements ValueObject<MenusId>, Serializable {
    /**
     * 菜单或功能id
     */
    @NonNull
    private Integer id;

    @Override
    public boolean sameValueAs(MenusId other) {
        return Objects.equals(this, other);
    }
}
