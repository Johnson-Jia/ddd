package com.tbc.ddd.bff.role.converter;

import com.tbc.ddd.domain.role.enums.MenusTypeEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tbc.ddd.domain.role.dto.MenusDTO;
import com.tbc.ddd.infrastructure.role.entity.RoleMenusPO;
import org.mapstruct.Named;

/**
 * 菜单转换器
 *
 * @author Johnson.Jia
 * @date 2023/3/22 13:04:25
 */
@Mapper(componentModel = "spring")
public interface RoleOrMenusConverter {

    /**
     * po 转 dto
     *
     * @author Johnson.Jia
     * @date 2023/3/22 13:05:15
     * @param roleMenusPO
     * @return
     */
    @Mapping(target = "status",
        // java表达式 示例
        expression = "java(com.tbc.ddd.domain.role.enums.MenusStatusEnum.valueOf(roleMenusPO.getStatus()))")
    @Mapping(source = "type", target = "type", qualifiedByName = "toType")
    MenusDTO toMenusDto(RoleMenusPO roleMenusPO);

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

}
