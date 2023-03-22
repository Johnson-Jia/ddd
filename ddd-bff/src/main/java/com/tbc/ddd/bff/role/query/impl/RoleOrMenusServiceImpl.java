package com.tbc.ddd.bff.role.query.impl;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tbc.ddd.bff.role.converter.RoleOrMenusConverter;
import com.tbc.ddd.bff.role.model.query.MenusQuery;
import com.tbc.ddd.bff.role.query.RoleOrMenusService;
import com.tbc.ddd.domain.role.dto.MenusDTO;
import com.tbc.ddd.domain.role.enums.MenusStatusEnum;
import com.tbc.ddd.infrastructure.role.entity.RoleMenusPO;
import com.tbc.ddd.infrastructure.role.mapper.RoleMenusMapper;

import lombok.RequiredArgsConstructor;

/**
 * 角色or菜单接口
 *
 * @author Johnson.Jia
 * @date 2023/3/22 10:23:34
 */
@Service
@RequiredArgsConstructor
public class RoleOrMenusServiceImpl implements RoleOrMenusService {

    final RoleMenusMapper roleMenusMapper;
    final RoleOrMenusConverter roleOrMenusConverter;

    @Override
    public IPage<MenusDTO> queryListByPage(MenusQuery menusQuery) {
        LambdaQueryWrapper<RoleMenusPO> eq = null;
        if (menusQuery.getStatus() != null && menusQuery.getStatus() != MenusStatusEnum.UNKNOWN) {
            eq = Wrappers.<RoleMenusPO>lambdaQuery().eq(RoleMenusPO::getStatus, menusQuery.getStatus().getCode());
        }
        Page<RoleMenusPO> page = roleMenusMapper.selectPage(menusQuery.toPage(), eq);
        return page.convert(roleOrMenusConverter::toMenusDto);
    }

}
