package com.tbc.ddd.domain.south.role.repository;

import com.tbc.ddd.domain.role.model.Role;
import com.tbc.ddd.domain.role.model.RoleId;

/**
 * 用户角色 仓储服务
 *
 * @author Johnson.Jia
 * @date 2023/3/15 15:44:33
 */
public interface RoleRepository {
    /**
     * 保存角色
     *
     * @author Johnson.Jia
     * @date 2023/3/15 16:23:09
     * @param role
     * @return
     */
    Role save(Role role);

    /**
     * 获取角色信息
     *
     * @author Johnson.Jia
     * @date 2023/3/15 16:39:54
     * @param roleId
     *            角色Id
     * @return
     */
    Role getRoleById(RoleId roleId);
}
