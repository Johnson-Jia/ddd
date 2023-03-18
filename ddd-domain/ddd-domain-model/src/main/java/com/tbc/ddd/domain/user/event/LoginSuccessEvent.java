package com.tbc.ddd.domain.user.event;

import com.tbc.ddd.domain.event.BaseDomainEvent;
import com.tbc.ddd.domain.event.DomainEventEnum;
import com.tbc.ddd.domain.user.model.Login;

/**
 * 登录成功事件
 *
 * @author Johnson.Jia
 * @date 2023/3/18 17:22:10
 */
public class LoginSuccessEvent extends BaseDomainEvent<Login> {
    private static final long serialVersionUID = 6852692633678888085L;

    public LoginSuccessEvent(Login data) {
        super(data, DomainEventEnum.LOGIN_SUCCESS);
    }
}
