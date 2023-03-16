package com.tbc.ddd.domain.role.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.tbc.ddd.common.ddd.Entity;
import com.tbc.ddd.domain.role.enums.MenusStatusEnum;
import com.tbc.ddd.domain.role.enums.MenusTypeEnum;

import lombok.*;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author Johnson.Jia
 * @since 2023-03-15 15:42:55
 */
@Data
@Builder
@Setter(AccessLevel.PRIVATE)
public class MenusDO implements Entity, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单或功能id
     */
    private MenusId menusId;

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
     * 父级 菜单
     */
    private MenusId parentId;

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
     * 创建时间
     */
    private LocalDateTime createTime;

}
