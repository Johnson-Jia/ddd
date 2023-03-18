package com.tbc.ddd.infrastructure.role.converter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tbc.ddd.domain.role.model.Role;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.tbc.ddd.domain.role.model.MenusId;
import com.tbc.ddd.infrastructure.role.entity.RolePO;

/**
 * 用户角色 对象转换
 *
 * @author Johnson.Jia
 * @date 2023/3/15 11:46:22
 */
@Mapper(componentModel = "spring")
public interface RoleConverter {

    /**
     * do 转 po
     *
     * @author Johnson.Jia
     * @param role
     * @return
     */
    @Mapping(source = "roleId.id", target = "id")
    @Mapping(target = "menus",
        // java表达式 示例
        expression = "java(role.getMenus().stream().map(menus -> menus.getId().toString()).collect(java.util.stream.Collectors.joining(\",\")))")
    RolePO toRolePo(Role role);

    /**
     * PO 转 DO
     *
     * @author Johnson.Jia
     * @param rolePO
     * @return
     */
    @Mapping(source = "id", target = "roleId.id")
    @Mapping(source = "menus", target = "menus",
        // 方法示例
        qualifiedByName = "toMenus")
    Role toRole(RolePO rolePO);

    /**
     * 转换菜单对象
     *
     * @author Johnson.Jia
     * @date 2023/3/16 11:48:23
     * @param menus
     * @return
     */
    @Named("toMenus")
    default List<MenusId> toMenus(String menus) {
        if (StringUtils.isBlank(menus)) {
            return null;
        }
        return Stream.of(menus.split(",")).map(id -> MenusId.builder().id(Integer.parseInt(id)).build())
            .collect(Collectors.toList());
    }

}
