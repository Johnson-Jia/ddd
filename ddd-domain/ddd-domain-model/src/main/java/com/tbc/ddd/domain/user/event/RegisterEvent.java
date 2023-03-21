package com.tbc.ddd.domain.user.event;

import com.tbc.ddd.common.event.BaseDomainEvent;
import com.tbc.ddd.domain.event.EventEnum;
import com.tbc.ddd.domain.user.dto.UserDTO;

/**
 * 注册成功事件对象
 *
 * @author Johnson.Jia
 */
public class RegisterEvent extends BaseDomainEvent<UserDTO> {
    private static final long serialVersionUID = 6852692633678888085L;

    public RegisterEvent(UserDTO data) {
        super(data, EventEnum.REGISTER_SUCCESS);
    }

}
