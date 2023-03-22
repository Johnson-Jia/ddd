package com.tbc.ddd.domain.south.role.repository;

import com.tbc.ddd.common.ddd.Repository;
import com.tbc.ddd.domain.role.model.Role;
import com.tbc.ddd.domain.role.valueobject.RoleId;

/**
 * 用户角色 仓储服务
 *
 * @author Johnson.Jia
 * @date 2023/3/15 15:44:33
 */
public interface RoleRepository extends Repository<Role, RoleId> {
}
