package com.tbc.ddd.domain.user.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.tbc.ddd.common.utils.Consts;

import lombok.extern.slf4j.Slf4j;

/**
 * 注册成功事件监听
 *
 * @author Johnson.Jia
 */
@Slf4j
@Component
public class RegisterEventListener {

    @Async
    @EventListener(RegisterEvent.class)
    public void loginEvent(RegisterEvent event) {
        log.info("注册成功,event:{}", Consts.objectToString(event));
    }
}
