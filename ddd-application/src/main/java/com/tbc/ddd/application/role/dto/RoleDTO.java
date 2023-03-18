package com.tbc.ddd.application.role.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * 角色信息展示对象
 *
 * @author Johnson.Jia
 * @date 2023/3/17 14:51:17
 */
@Data
@Builder
public class RoleDTO {

    /**
     * 角色ID
     */
    private Integer id;

    /**
     * 角色 名称
     */
    private String name;

    /**
     * 菜单列表、功能列表 子集
     */
    private List<MenusDTO> list;

}
