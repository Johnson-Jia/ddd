package com.tbc.ddd.common.event;

import java.io.Serializable;
import java.util.UUID;

import com.tbc.ddd.common.ddd.EventInterface;

import lombok.Getter;
import lombok.Setter;

/**
 * 领域事件基类
 *
 * @author Johnson.Jia
 * @date 2023/3/17 16:13:51
 */
@Getter
@Setter
public abstract class BaseDomainEvent<T> implements Serializable {

    private static final long serialVersionUID = -3216369957371516518L;

    /**
     * 幂等键:即为当前事件的id
     */
    private String id;

    /**
     * 事件类型
     */
    private EventInterface eventType;

    /**
     * 事件 创建时间戳
     */
    private long timestamp;

    /**
     * 领域事件数据
     */
    private T data;

    public BaseDomainEvent(T data) {
        this(data, DefaultEventEnum.UNKNOWN);
    }

    public BaseDomainEvent(T data, EventInterface eventType) {
        this(data, UUID.randomUUID().toString().replace("-", ""), eventType);
    }

    public BaseDomainEvent(T data, String id, EventInterface eventType) {
        this.data = data;
        this.id = id;
        this.eventType = eventType;
        this.timestamp = System.currentTimeMillis();
    }

}
