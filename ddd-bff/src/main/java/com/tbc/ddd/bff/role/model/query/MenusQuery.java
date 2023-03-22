package com.tbc.ddd.bff.role.model.query;

import org.apache.commons.lang3.StringUtils;

import com.tbc.ddd.common.bean.PageQuery;
import com.tbc.ddd.domain.role.enums.MenusStatusEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜单分页查询对象
 *
 * @author Johnson.Jia
 * @date 2023/3/22 10:50:25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MenusQuery extends PageQuery {

    {// 初始化排序字段列表
        this.ORDER_MAP.put("id", "id");
        this.ORDER_MAP.put("parentId", "parent_id");
        this.ORDER_MAP.put("type", "type");
        this.ORDER_MAP.put("status", "status");
    }

    /**
     * 菜单状态
     *
     * @author Johnson.Jia
     * @date 2023/3/22 11:06:40
     */
    private MenusStatusEnum status;

    @Override
    public String getOrder() {
        String order = super.getOrder();
        if (StringUtils.isBlank(order)) {
            // 返回默认排序字段
            return "id";
        }
        return order;
    }
}
