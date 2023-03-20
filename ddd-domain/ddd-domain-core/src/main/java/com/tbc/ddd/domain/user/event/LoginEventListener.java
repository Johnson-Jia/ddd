package com.tbc.ddd.domain.user.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.tbc.ddd.common.utils.Consts;

import lombok.extern.slf4j.Slf4j;

/**
 * 登录成功事件监听
 *
 * @author Johnson.Jia
 * @date 2023/3/18 17:36:48
 */
@Slf4j
@Component
public class LoginEventListener {

    @Async
    @EventListener(LoginEvent.class)
    public void loginEvent(LoginEvent event) {
        log.info("登录成功,event:{}", Consts.objectToString(event));
    }
}
