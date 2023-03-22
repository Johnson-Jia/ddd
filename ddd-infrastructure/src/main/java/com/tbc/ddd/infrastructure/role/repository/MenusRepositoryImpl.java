package com.tbc.ddd.infrastructure.role.repository;

import com.tbc.ddd.domain.role.model.Menus;
import com.tbc.ddd.domain.role.valueobject.MenusId;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.tbc.ddd.domain.south.role.repository.MenusRepository;
import com.tbc.ddd.infrastructure.role.converter.MenusConverter;
import com.tbc.ddd.infrastructure.role.entity.RoleMenusPO;
import com.tbc.ddd.infrastructure.role.mapper.RoleMenusMapper;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public Menus getById(MenusId menusId) {
        return menusConverter.toMenus(roleMenusMapper.selectById(menusId.getId()));
    }

    @Override
    public Menus save(Menus menus) {
        RoleMenusPO menusPo = menusConverter.toMenusPo(menus);
        menusPo.setCreateTime(LocalDateTime.now());
        roleMenusMapper.insert(menusPo);
        return menusConverter.toMenus(menusPo);
    }

    @Override
    public Menus update(Menus menus) {
        RoleMenusPO menusPo = menusConverter.toMenusPo(menus);
        roleMenusMapper.updateById(menusPo);
        return menusConverter.toMenus(menusPo);
    }

    @Override
    public List<Menus> getListByIds(List<MenusId> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<RoleMenusPO> roleMenus =
            roleMenusMapper.selectBatchIds(list.stream().map(MenusId::getId).collect(Collectors.toList()));
        return menusConverter.toMenusList(roleMenus);
    }
}
