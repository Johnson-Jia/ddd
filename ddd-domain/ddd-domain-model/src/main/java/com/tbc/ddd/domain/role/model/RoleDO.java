package com.tbc.ddd.domain.role.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.tbc.ddd.common.ddd.Entity;
import lombok.*;

/**
 * <p>
 * 用户角色表
 * </p>
 *
 * @author Johnson.Jia
 * @since 2023-03-15 15:42:55
 */
@Data
@Builder
@Setter(AccessLevel.PRIVATE)
public class RoleDO implements Entity, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    private RoleId roleId;

    /**
     * 角色 名称
     */
    private String name;

    /**
     * 角色 拥有 菜单 ( 功能 ) 多个英文逗号分割 “,”
     */
    private List<MenusId> menus;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
