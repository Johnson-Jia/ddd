package com.tbc.ddd.infrastructure.user.converter;

import com.tbc.ddd.domain.user.enums.GenderEnum;
import com.tbc.ddd.domain.user.model.UserInfoDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tbc.ddd.infrastructure.user.entity.UserInfoPO;
import org.mapstruct.Named;

/**
 * 用户详情 转换器
 *
 * @author Johnson.Jia
 * @date 2023/3/15 11:35:00
 */
@Mapper(componentModel = "spring")
public interface UserInfoConverter {
    /**
     * 用户详情转换
     *
     * @author Johnson.Jia
     * @date 2023/3/15 11:39:10
     * @param userInfoDO
     * @return
     */
    @Mapping(source = "address.country", target = "country")
    @Mapping(source = "address.province", target = "province")
    @Mapping(source = "address.city", target = "city")
    @Mapping(source = "address.address", target = "address")
    @Mapping(source = "gender.code", target = "gender")
    @Mapping(source = "userId.id", target = "userId")
    UserInfoPO toUserInfoPo(UserInfoDO userInfoDO);

    /**
     * 用户详情对象 转换
     *
     * @author Johnson.Jia
     * @date 2023/3/15 11:43:31
     * @param userInfoPO
     * @return
     */
    @Mapping(source = "country", target = "address.country")
    @Mapping(source = "province", target = "address.province")
    @Mapping(source = "city", target = "address.city")
    @Mapping(source = "address", target = "address.address")
    @Mapping(source = "gender", target = "gender", qualifiedByName = "toGenderEnum")
    @Mapping(source = "userId", target = "userId.id")
    UserInfoDO toUserInfoDo(UserInfoPO userInfoPO);

    /**
     * 性别转换
     *
     * @author Johnson.Jia
     * @date 2023/3/16 14:25:08
     * @param code
     * @return
     */
    @Named("toGenderEnum")
    default GenderEnum toGenderEnum(int code) {
        return GenderEnum.valueOf(code);
    }

}
