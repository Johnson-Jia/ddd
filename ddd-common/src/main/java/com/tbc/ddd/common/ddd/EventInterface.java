package com.tbc.ddd.common.ddd;

/**
 * 领域事件接口
 *
 * @author Johnson.Jia
 * @date 2023/3/21 11:54:57
 */
public interface EventInterface {

    /**
     * 获取 事件 key 标识
     *
     * @author Johnson.Jia
     * @return
     */
    String getKey();

    /**
     * 获取事件名称
     *
     * @author Johnson.Jia
     * @return
     */
    String getName();

}
