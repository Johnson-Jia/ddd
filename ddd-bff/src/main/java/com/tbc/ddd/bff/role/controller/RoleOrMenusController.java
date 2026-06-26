package com.tbc.ddd.bff.role.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tbc.ddd.bff.role.model.query.MenusQuery;
import com.tbc.ddd.bff.role.query.RoleOrMenusService;
import com.tbc.ddd.bff.user.converter.UserConverter;
import com.tbc.ddd.bff.vo.MenusVO;
import com.tbc.ddd.common.bean.Result;
import com.tbc.ddd.common.spring.BaseController;
import com.tbc.ddd.domain.north.role.dto.MenusDTO;

import lombok.RequiredArgsConstructor;

/**
 * 角色 菜单
 *
 * @author Johnson.Jia
 * @date 2023/3/22 12:41:48
 */
@RestController
@RequiredArgsConstructor
public class RoleOrMenusController extends BaseController {

    final RoleOrMenusService roleOrMenusService;
    final UserConverter userConverter;

    @PostMapping("/getMenusList")
    public Result<IPage<MenusVO>> getMenusList(@RequestBody MenusQuery menusQuery) {
        IPage<MenusDTO> page = roleOrMenusService.queryListByPage(menusQuery);
        return Result.ok(page.convert(userConverter::toMenusVO));
    }

}
