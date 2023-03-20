package com.tbc.ddd.domain.role.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.tbc.ddd.common.ddd.AggregateRoot;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

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
public class Role implements AggregateRoot {

    /**
     * 角色ID
     */
    private RoleId roleId;

    /**
     * 角色 名称
     */
    private String name;

    /**
     * 角色 拥有 菜单 ( 功能 )
     */
    private List<MenusId> menus;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 菜单列表、功能列表 子集
     */
    private List<Menus> list;

    /**
     * 生成菜单树
     *
     * @author Johnson.Jia
     * @date 2023/3/16 14:46:43
     * @param list
     *            当前角色菜单集合
     * @return
     */
    public void createMenusTree(List<Menus> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<Menus> tree = menusRecursion(list.stream().filter(Menus::isMenus).collect(Collectors.toList()),
            MenusId.builder().id(0).build());
        tree.forEach(menus1 -> menus1.setButtonCode(list));
        this.list = tree;
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
    private List<Menus> menusRecursion(List<Menus> list, MenusId parentId) {
        List<Menus> result = new ArrayList<>();
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
