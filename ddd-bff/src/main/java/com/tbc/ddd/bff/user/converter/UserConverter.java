package com.tbc.ddd.bff.user.converter;

import org.mapstruct.Mapper;

import com.tbc.ddd.bff.user.model.req.UserRegisterReq;
import com.tbc.ddd.domain.user.dto.UserRegisterDTO;

/**
 * 用户登录信息 对象转换
 *
 * @author Johnson.Jia
 * @date 2023/3/15 11:46:22
 */
@Mapper(componentModel = "spring")
public interface UserConverter {

    /**
     * req 转 dto
     *
     * @param userRegisterReq
     * @return
     * @author Johnson.Jia
     */
    UserRegisterDTO toUserRegisterDto(UserRegisterReq userRegisterReq);

}
