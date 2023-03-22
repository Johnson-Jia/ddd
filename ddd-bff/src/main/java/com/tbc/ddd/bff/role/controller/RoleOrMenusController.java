package com.tbc.ddd.bff.role.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tbc.ddd.bff.role.model.query.MenusQuery;
import com.tbc.ddd.bff.role.query.RoleOrMenusService;
import com.tbc.ddd.common.bean.Result;
import com.tbc.ddd.common.spring.BaseController;
import com.tbc.ddd.domain.role.dto.MenusDTO;

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

    @PostMapping("/getMenusList")
    public Result<IPage<MenusDTO>> getMenusList(@RequestBody MenusQuery menusQuery) {
        return Result.ok(roleOrMenusService.queryListByPage(menusQuery));
    }

}
