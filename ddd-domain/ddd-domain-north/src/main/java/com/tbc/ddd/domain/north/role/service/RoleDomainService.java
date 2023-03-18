package com.tbc.ddd.domain.north.role.service;

import com.tbc.ddd.domain.role.model.Menus;
import com.tbc.ddd.domain.role.model.Role;

import java.util.List;

/**
 * 菜单 领域服务接口
 *
 * @author Johnson.Jia
 * @date 2023/3/17 14:42:46
 */
public interface RoleDomainService {

    /**
     * 创建角色 菜单树
     *
     * @author Johnson.Jia
     * @date 2023/3/17 14:47:45
     * @param role
     *            角色信息
     * @param menus
     *            当前角色所有 菜单列表
     * @return 带有菜单树的角色信息
     */
    Role createRoleMenusTree(Role role, List<Menus> menus);
}
