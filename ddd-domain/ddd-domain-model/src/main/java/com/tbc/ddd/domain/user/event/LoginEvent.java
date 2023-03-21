package com.tbc.ddd.domain.user.event;

import com.tbc.ddd.common.event.BaseDomainEvent;
import com.tbc.ddd.domain.event.EventEnum;
import com.tbc.ddd.domain.user.model.Login;

/**
 * 登录成功事件对象
 *
 * @author Johnson.Jia
 * @date 2023/3/18 17:22:10
 */
public class LoginEvent extends BaseDomainEvent<Login> {
    private static final long serialVersionUID = 6852692633678888085L;

    public LoginEvent(Login data) {
        super(data, EventEnum.LOGIN_SUCCESS);
    }

}
