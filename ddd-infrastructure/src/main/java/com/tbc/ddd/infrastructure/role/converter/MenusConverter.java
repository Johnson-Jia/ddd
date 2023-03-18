package com.tbc.ddd.infrastructure.role.converter;

import com.tbc.ddd.domain.role.model.Menus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.tbc.ddd.domain.role.enums.MenusTypeEnum;
import com.tbc.ddd.infrastructure.role.entity.RoleMenusPO;

import java.util.List;

/**
 * 菜单 对象转换
 *
 * @author Johnson.Jia
 * @date 2023/3/15 11:46:22
 */
@Mapper(componentModel = "spring")
public interface MenusConverter {

    /**
     * do 转 po
     *
     * @author Johnson.Jia
     * @date 2023/3/15 15:11:36
     * @param menus
     * @return
     */
    @Mapping(source = "status.code", target = "status")
    @Mapping(source = "type.code", target = "type")
    @Mapping(source = "parentId.id", target = "parentId")
    @Mapping(source = "menusId.id", target = "id")
    RoleMenusPO toMenusPo(Menus menus);

    /**
     * PO 转 DO
     *
     * @author Johnson.Jia
     * @date 2023/3/15 15:11:50
     * @param menusPO
     * @return
     */
    @Mapping(target = "status",
        // java表达式 示例
        expression = "java(com.tbc.ddd.domain.role.enums.MenusStatusEnum.valueOf(menusPO.getStatus()))")
    @Mapping(source = "type", target = "type", qualifiedByName = "toType")
    @Mapping(source = "parentId", target = "parentId.id")
    @Mapping(source = "id", target = "menusId.id")
    Menus toMenus(RoleMenusPO menusPO);

    /**
     * 类型枚举
     *
     * @author Johnson.Jia
     * @date 2023/3/16 14:39:42
     * @param code
     * @return
     */
    @Named("toType")
    default MenusTypeEnum toType(int code) {
        return MenusTypeEnum.valueOf(code);
    }

    /**
     * 集合 转换
     *
     * @author Johnson.Jia
     * @date 2023/3/16 16:24:59
     * @param menusPO
     * @return
     */
    List<Menus> toMenusList(List<RoleMenusPO> menusPO);

}
