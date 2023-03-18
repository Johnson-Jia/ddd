package com.tbc.ddd.domain.user.event;

import com.tbc.ddd.common.utils.Consts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 登录成功事件监听
 *
 * @author Johnson.Jia
 * @date 2023/3/18 17:36:48
 */
@Slf4j
@Component
public class LoginSuccessEventListener implements ApplicationListener<LoginSuccessEvent> {

    @Override
    public void onApplicationEvent(LoginSuccessEvent event) {

        log.info("登录成功,event:{}", Consts.objectToString(event));
    }
}
