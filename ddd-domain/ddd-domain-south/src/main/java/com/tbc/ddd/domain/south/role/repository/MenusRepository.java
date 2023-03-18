package com.tbc.ddd.domain.south.role.repository;

import com.tbc.ddd.common.ddd.Repository;
import com.tbc.ddd.domain.role.model.Menus;
import com.tbc.ddd.domain.role.model.MenusId;

import java.util.List;

/**
 * 角色菜单 仓储服务
 *
 * @author Johnson.Jia
 * @date 2023/3/15 15:44:45
 */
public interface MenusRepository extends Repository<Menus, MenusId> {

    /**
     * 查询菜单集合
     *
     * @author Johnson.Jia
     * @date 2023/3/17 13:22:17
     * @param list
     *            id集合
     * @return
     */
    List<Menus> getListByIds(List<MenusId> list);

}
