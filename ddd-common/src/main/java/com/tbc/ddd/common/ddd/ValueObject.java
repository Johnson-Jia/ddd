package com.tbc.ddd.common.ddd;

/**
 * 值对象标记接口
 *
 * @author Johnson.Jia
 */
public interface ValueObject<T> extends MarkerInterface {

    /**
     * 值对象通过属性比较
     *
     * @param other
     * @return
     */
    boolean sameValueAs(T other);

}
