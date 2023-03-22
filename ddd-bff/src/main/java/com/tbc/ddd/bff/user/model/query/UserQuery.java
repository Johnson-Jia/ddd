package com.tbc.ddd.bff.user.model.query;

import org.apache.commons.lang3.StringUtils;

import com.tbc.ddd.common.bean.PageQuery;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户分页查询对象
 *
 * @author Johnson.Jia
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserQuery extends PageQuery {

    {// 初始化排序字段列表
        this.ORDER_MAP.put("userId", "user_id");
        this.ORDER_MAP.put("createTime", "create_time");
    }

    @Override
    public String getOrder() {
        String order = super.getOrder();
        if (StringUtils.isBlank(order)) {
            // 返回默认排序字段
            return "createTime";
        }
        return order;
    }
}
