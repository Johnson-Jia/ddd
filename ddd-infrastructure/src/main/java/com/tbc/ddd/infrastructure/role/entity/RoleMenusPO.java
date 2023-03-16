package com.tbc.ddd.infrastructure.role.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.*;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author Johnson.Jia
 * @since 2023-03-16 13:45:49
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode()
@TableName("t_uc_role_menus")
public class RoleMenusPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 菜单 或 功能点名称
     */
    @TableField("name")
    private String name;

    /**
     * 请求访问 url
     */
    @TableField("url")
    private String url;

    /**
     * 后端 接口url
     */
    @TableField("interface_url")
    private String interfaceUrl;

    /**
     * 未选中状态 菜单icon 图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 选中状态 菜单icon图标
     */
    @TableField("re_icon")
    private String reIcon;

    /**
     * 父级 菜单或功能 id
     */
    @TableField("parent_id")
    private Integer parentId;

    /**
     * 菜单 类型    0：菜单   1：功能点 按钮等
     */
    @TableField("type")
    private Integer type;

    /**
     * 功能编码 对应前端编号
     */
    @TableField("code")
    private String code;

    /**
     * 菜单状态    0：隐藏  1：开启
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}
