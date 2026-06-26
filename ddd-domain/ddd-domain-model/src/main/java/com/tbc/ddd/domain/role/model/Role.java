package com.tbc.ddd.domain.role.model;

import java.time.LocalDateTime;
import java.util.List;

import com.tbc.ddd.common.ddd.AggregateRoot;
import com.tbc.ddd.domain.role.valueobject.MenusId;
import com.tbc.ddd.domain.role.valueobject.RoleId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

/**
 * <p>
 * 用户角色表(聚合根)
 * </p>
 * <p>
 * 聚合根只承载自身持久状态(roleId/name/menus/createTime)；
 * 菜单树组装属展示逻辑，由应用层负责，不再挂在 Role 上。
 *
 * @author Johnson.Jia
 * @since 2023-03-15 15:42:55
 */
@Data
@Builder
@Setter(AccessLevel.PRIVATE)
public class Role implements AggregateRoot {

    /**
     * 角色ID
     */
    private RoleId roleId;

    /**
     * 角色 名称
     */
    private String name;

    /**
     * 角色 拥有 菜单 ( 功能 )
     */
    private List<MenusId> menus;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
