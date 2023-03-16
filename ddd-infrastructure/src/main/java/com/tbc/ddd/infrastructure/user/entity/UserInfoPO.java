package com.tbc.ddd.infrastructure.user.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.*;

/**
 * <p>
 * 用户详情表
 * </p>
 *
 * @author Johnson.Jia
 * @since 2023-03-15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode()
@TableName("t_uc_user_info")
public class UserInfoPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId("user_id")
    private Long userId;

    /**
     * 用户昵称
     */
    @TableField("nick_name")
    private String nickName;

    /**
     * 国家
     */
    @TableField("country")
    private String country;

    /**
     * 用户 所在省份
     */
    @TableField("province")
    private String province;

    /**
     * 用户所在城市
     */
    @TableField("city")
    private String city;

    /**
     * 用户详细地址
     */
    @TableField("address")
    private String address;

    /**
     * 用户性别 1：男性 0：女性
     */
    @TableField("gender")
    private Integer gender;

    /**
     * 用户头像 地址
     */
    @TableField("avatar_url")
    private String avatarUrl;

    /**
     * 身份证 号码
     */
    @TableField("identity_card")
    private String identityCard;

    /**
     * 百度云人脸face token 标识
     */
    @TableField("face_token")
    private String faceToken;

    /**
     * 人脸识别图片url列表 多张 ";" 分号分割。 上传到 阿里云oss
     */
    @TableField("face_image_url")
    private String faceImageUrl;

    /**
     * 创建时间 时间戳
     */
    @TableField("create_time")
    private Long createTime;
}
