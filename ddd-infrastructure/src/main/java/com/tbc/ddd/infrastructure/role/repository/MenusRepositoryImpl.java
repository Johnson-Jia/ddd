package com.tbc.ddd.infrastructure.role.repository;

import com.tbc.ddd.domain.role.model.MenusDO;
import com.tbc.ddd.domain.role.model.MenusId;
import org.springframework.stereotype.Repository;

import com.tbc.ddd.domain.south.role.repository.MenusRepository;
import com.tbc.ddd.infrastructure.role.converter.MenusConverter;
import com.tbc.ddd.infrastructure.role.entity.RoleMenusPO;
import com.tbc.ddd.infrastructure.role.mapper.RoleMenusMapper;

import lombok.RequiredArgsConstructor;

/**
 * 角色菜单 仓储服务 实现
 *
 * @author Johnson.Jia
 * @date 2023/3/15 15:46:38
 */
@Repository
@RequiredArgsConstructor
public class MenusRepositoryImpl implements MenusRepository {
    final RoleMenusMapper roleMenusMapper;
    final MenusConverter menusConverter;

    @Override
    public void deleteById(MenusId menusId) {
        roleMenusMapper.deleteById(menusId.getId());
    }

    @Override
    public MenusDO getById(MenusId menusId) {
        return menusConverter.toMenus(roleMenusMapper.selectById(menusId.getId()));
    }

    @Override
    public MenusDO save(MenusDO menusDO) {
        RoleMenusPO menusPO = menusConverter.toMenusPo(menusDO);
        if (menusPO.getId() == null) {
            roleMenusMapper.insert(menusPO);
        } else {
            roleMenusMapper.updateById(menusPO);
        }
        return menusConverter.toMenus(menusPO);
    }
}
