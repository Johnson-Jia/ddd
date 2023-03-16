package com.tbc.ddd.infrastructure.role.repository;

import com.tbc.ddd.domain.role.model.RoleDO;
import org.springframework.stereotype.Repository;

import com.tbc.ddd.domain.role.model.RoleId;
import com.tbc.ddd.domain.south.role.repository.RoleRepository;
import com.tbc.ddd.infrastructure.role.converter.RoleConverter;
import com.tbc.ddd.infrastructure.role.entity.RolePO;
import com.tbc.ddd.infrastructure.role.mapper.RoleMapper;

import lombok.RequiredArgsConstructor;

/**
 * 用户角色 仓储服务 实现
 *
 * @author Johnson.Jia
 * @date 2023/3/15 15:46:01
 */
@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {
    final RoleMapper roleMapper;
    final RoleConverter roleConverter;

    @Override
    public RoleDO save(RoleDO roleDO) {
        RolePO rolePO = roleConverter.toRolePo(roleDO);
        if (rolePO.getId() == null) {
            roleMapper.insert(rolePO);
        } else {
            roleMapper.updateById(rolePO);
        }
        return roleConverter.toRoleDo(rolePO);
    }

    @Override
    public RoleDO getRoleById(RoleId roleId) {
        RolePO rolePO = roleMapper.selectById(roleId.getId());
        return roleConverter.toRoleDo(rolePO);
    }
}
