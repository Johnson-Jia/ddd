package com.tbc.ddd.application.role.dto;

import java.util.List;

import com.tbc.ddd.domain.role.enums.MenusStatusEnum;
import com.tbc.ddd.domain.role.enums.MenusTypeEnum;
import com.tbc.ddd.domain.role.model.Menus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色信息VO
 *
 * @author Johnson.Jia
 * @date 2023/3/17 14:53:38
 */
@Data
@Builder
public class MenusDTO {

    /**
     * 菜单或功能id
     */
    private Integer id;

    /**
     * 菜单 或 功能点名称
     */
    private String name;

    /**
     * 请求访问 url
     */
    private String url;

    /**
     * 后端 接口url
     */
    private String interfaceUrl;

    /**
     * 未选中状态 菜单icon 图标
     */
    private String icon;

    /**
     * 选中状态 菜单icon图标
     */
    private String reIcon;

    /**
     * 父级 菜单id
     */
    private Integer parentId;

    /**
     * 菜单 类型
     */
    private MenusTypeEnum type;

    /**
     * 功能编码 对应前端编号
     */
    private String code;

    /**
     * 菜单状态
     */
    private MenusStatusEnum status;

    /**
     * 菜单列表、功能列表 子集
     */
    private List<Menus> list;
}
