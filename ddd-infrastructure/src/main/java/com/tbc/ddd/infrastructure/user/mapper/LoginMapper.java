package com.tbc.ddd.infrastructure.user.mapper;

import com.tbc.ddd.infrastructure.user.entity.LoginPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户登录信息表 Mapper 接口
 * </p>
 *
 * @author Johnson.Jia
 * @since 2023-03-15
 */
@Mapper
public interface LoginMapper extends BaseMapper<LoginPO> {

}
