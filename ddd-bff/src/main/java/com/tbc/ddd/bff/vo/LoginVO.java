package com.tbc.ddd.bff.vo;

import com.tbc.ddd.common.bean.Secret;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录信息 VO
 *
 * @author Johnson.Jia
 */
@Data
@Builder
public class LoginVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String phone;
    private String loginName;
    private Long createTime;
    private Secret secret;
}
