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
 * 用户角色表
 * </p>
 *
 * @author Johnson.Jia
 * @since 2023-03-15 15:42:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode()
@TableName("t_uc_role")
public class RolePO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 角色 名称
     */
    @TableField("name")
    private String name;

    /**
     * 角色 拥有 菜单 ( 功能 )  多个英文逗号分割 “,”
     */
    @TableField("menus")
    private String menus;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}
