package com.tbc.ddd.domain.role.service;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;

import com.tbc.ddd.domain.north.role.service.RoleDomainService;
import com.tbc.ddd.domain.role.model.Menus;
import com.tbc.ddd.domain.role.model.Role;

import lombok.RequiredArgsConstructor;

/**
 * 菜单 领域服务接口
 *
 * @author Johnson.Jia
 * @date 2023/3/17 14:43:06
 */
@DubboService
@RequiredArgsConstructor
public class RoleDomainServiceImpl implements RoleDomainService {

    @Override
    public Role createRoleMenusTree(Role role, List<Menus> menus) {
        role.createMenusTree(menus);
        return role;
    }
}
