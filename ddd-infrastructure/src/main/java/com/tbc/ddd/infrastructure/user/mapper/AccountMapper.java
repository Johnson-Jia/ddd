package com.tbc.ddd.infrastructure.user.mapper;

import com.tbc.ddd.infrastructure.user.entity.AccountPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户账户信息，涉及金额 分佣机制 Mapper 接口
 * </p>
 *
 * @author Johnson.Jia
 * @since 2023-03-15
 */
@Mapper
public interface AccountMapper extends BaseMapper<AccountPO> {

}
