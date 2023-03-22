package com.tbc.ddd.infrastructure.role.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.tbc.ddd.domain.role.model.Role;
import com.tbc.ddd.domain.role.valueobject.RoleId;
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
    public void deleteById(RoleId roleId) {
        roleMapper.deleteById(roleId.getId());
    }

    @Override
    public Role save(Role role) {
        RolePO rolePo = roleConverter.toRolePo(role);
        roleMapper.insert(rolePo);
        return roleConverter.toRole(rolePo);
    }

    @Override
    public Role update(Role role) {
        RolePO rolePo = roleConverter.toRolePo(role);
        roleMapper.updateById(rolePo);
        return roleConverter.toRole(rolePo);
    }

    @Override
    public Role getById(RoleId roleId) {
        RolePO rolePO = roleMapper.selectById(roleId.getId());
        return roleConverter.toRole(rolePO);
    }

    @Override
    public List<Role> getListByIds(List<RoleId> roleIds) {
        List<RolePO> list = roleMapper.selectBatchIds(roleIds.stream().map(RoleId::getId).collect(Collectors.toList()));
        return roleConverter.toRoleList(list);
    }
}
