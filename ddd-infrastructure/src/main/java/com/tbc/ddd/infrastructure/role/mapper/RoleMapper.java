package com.tbc.ddd.infrastructure.role.mapper;

import com.tbc.ddd.infrastructure.role.entity.RolePO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户角色表 Mapper 接口
 * </p>
 *
 * @author Johnson.Jia
 * @since 2023-03-16 13:45:49
 */
@Mapper
public interface RoleMapper extends BaseMapper<RolePO> {

}
