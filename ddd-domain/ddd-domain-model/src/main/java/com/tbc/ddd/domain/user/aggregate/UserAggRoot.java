package com.tbc.ddd.domain.user.aggregate;

import com.tbc.ddd.common.ddd.AggregateRoot;
import com.tbc.ddd.common.utils.EncryptUtil;
import com.tbc.ddd.domain.user.model.UserInfoDO;
import com.tbc.ddd.domain.user.model.LoginDO;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 用户对象聚合根
 *
 * @author Johnson.Jia
 * @date 2023/3/15 15:54:23
 */
@Data
@Builder
@Setter(AccessLevel.PRIVATE)
public class UserAggRoot implements AggregateRoot, Serializable {

    private static final long serialVersionUID = 632359961953659372L;

    /**
     * 登录对象信息
     *
     * @author Johnson.Jia
     * @date 2023/3/15 15:54:47
     */
    private LoginDO loginDO;

    /**
     * 用户角色详情信息
     *
     * @author Johnson.Jia
     * @date 2023/3/15 15:55:02
     */
    private UserInfoDO userInfoDO;

    public void setPassword(String password) {
        // 进行MD5加密
        this.loginDO.setPassword(EncryptUtil.MD5(password));
    }

    boolean checkPassword(String password) {
        if (StringUtils.isBlank(password)) {
            return false;
        }
        return EncryptUtil.MD5(password).equals(this.loginDO.getPassword());
    }
}
