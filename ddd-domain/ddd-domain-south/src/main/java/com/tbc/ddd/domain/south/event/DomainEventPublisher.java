package com.tbc.ddd.domain.south.event;

import com.tbc.ddd.common.event.BaseDomainEvent;

/**
 * 领域事件发布接口
 *
 * @author Johnson.Jia
 * @date 2023/3/17 16:13:36
 */
public interface DomainEventPublisher {

    /**
     * 发布事件
     *
     * @param event
     *            领域事件
     */
    void publishEvent(BaseDomainEvent event);

}
