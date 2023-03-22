package com.tbc.ddd.bff.role.query;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tbc.ddd.bff.role.model.query.MenusQuery;
import com.tbc.ddd.domain.role.dto.MenusDTO;

/**
 * 角色or菜单接口
 *
 * @author Johnson.Jia
 * @date 2023/3/22 10:15:23
 */
public interface RoleOrMenusService {

    /**
     * 分页查询 菜单列表
     *
     * @author Johnson.Jia
     * @date 2023/3/22 16:36:53
     * @param menusQuery
     * @return
     */
    IPage<MenusDTO> queryListByPage(MenusQuery menusQuery);
}
