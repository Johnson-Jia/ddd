package com.tbc.ddd.common.bean;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.Data;

/**
 * 分页查询 基础类对象
 *
 * @author Johnson.Jia
 * @date 2023/3/22 14:37:47
 */
@Data
public abstract class PageQuery {

    /**
     * 每页显示条数，默认 10
     */
    private long size = 10;

    /**
     * 当前页
     */
    private long current = 1;

    /**
     * 排序规则 true：升序 false：降序
     */
    private boolean asc;

    /**
     * 排序字段属性
     */
    private String order;

    /**
     * 默认排序 属性-字段 对应值 示例： user_id --> userId
     */
    protected final Map<String, String> ORDER_MAP = new HashMap<>();

    /**
     * 生成 mybatis-plus 分页对象
     */
    public Page toPage() {
        return toPage(true);
    }

    /**
     * 生成 mybatis-plus 分页对象
     */
    public Page toPage(boolean searchCount) {
        String order = this.getOrder();
        Page<Object> page = Page.of(this.getCurrent(), this.getSize(), searchCount);
        if (StringUtils.isNotBlank(order)) {
            page.setOrders(Collections.singletonList(
                this.isAsc() ? OrderItem.asc(this.ORDER_MAP.get(order)) : OrderItem.desc(this.ORDER_MAP.get(order))));
        }
        return page;
    }

}
