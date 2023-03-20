package com.tbc.ddd.domain.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.tbc.ddd.common.utils.Consts;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 领域事件发布实现类
 *
 * @author Johnson.Jia
 * @date 2023/3/17 16:16:31
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DomainEventPublisherImpl implements DomainEventPublisher {

    final private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publishEvent(BaseDomainEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("发布事件,event:{}", Consts.objectToString(event));
        }
        applicationEventPublisher.publishEvent(event);
    }
}
