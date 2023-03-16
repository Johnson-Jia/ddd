package com.tbc.ddd.domain.role.aggregate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.tbc.ddd.common.ddd.AggregateRoot;
import com.tbc.ddd.domain.role.model.MenusId;
import com.tbc.ddd.domain.role.model.RoleId;

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
public class RoleAggRoot implements AggregateRoot, Serializable {

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
    private List<MenusAggRoot> menus;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 生成菜单树
     *
     * @author Johnson.Jia
     * @date 2023/3/16 14:46:43
     * @return
     */
    public void createMenusTree() {
        List<MenusAggRoot> tree =
            menusRecursion(this.menus.stream().filter(MenusAggRoot::isMenus).collect(Collectors.toList()),
                MenusId.builder().id(0).build());
        tree.forEach(menus1 -> menus1.setButtonCode(this.menus));
        this.menus = tree;
    }

    /**
     * 菜单 树 递归
     *
     * @author Johnson.Jia
     * @param list
     *            递归 菜单 集合
     * @param parentId
     *            父级id
     */
    private List<MenusAggRoot> menusRecursion(List<MenusAggRoot> list, MenusId parentId) {
        List<MenusAggRoot> result = new ArrayList<>();
        list.forEach(menus -> {
            if (menus.getParentId().equals(parentId) && menus.isEnable()) {
                menus.addMenusTree(menusRecursion(list, menus.getMenusId()));
                if (menus.isMenus()) {
                    result.add(menus);
                }
            }
        });
        return result;
    }

}
