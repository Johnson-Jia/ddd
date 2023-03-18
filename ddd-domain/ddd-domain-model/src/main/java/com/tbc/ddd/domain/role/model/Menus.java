package com.tbc.ddd.domain.role.model;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.tbc.ddd.common.ddd.AggregateRoot;
import com.tbc.ddd.domain.role.enums.MenusStatusEnum;
import com.tbc.ddd.domain.role.enums.MenusTypeEnum;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author Johnson.Jia
 * @since 2023-03-15 15:42:55
 */
@Data
@Builder
@Setter(AccessLevel.PRIVATE)
public class Menus implements AggregateRoot {

    /**
     * 菜单或功能id
     */
    private MenusId menusId;

    /**
     * 菜单 或 功能点名称
     */
    private String name;

    /**
     * 请求访问 url
     */
    private String url;

    /**
     * 后端 接口url
     */
    private String interfaceUrl;

    /**
     * 未选中状态 菜单icon 图标
     */
    private String icon;

    /**
     * 选中状态 菜单icon图标
     */
    private String reIcon;

    /**
     * 父级 菜单
     */
    private MenusId parentId;

    /**
     * 菜单 类型
     */
    private MenusTypeEnum type;

    /**
     * 功能编码 对应前端编号
     */
    private String code;

    /**
     * 菜单状态
     */
    private MenusStatusEnum status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 菜单列表、功能列表 子集
     */
    private List<Menus> list;

    /**
     * 添加按钮code
     *
     * @author Johnson.Jia
     * @date 2023/3/16 15:42:33
     * @param list
     * @return
     */
    public void setButtonCode(List<Menus> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            StringBuilder sb = new StringBuilder();
            list.forEach(menus -> {
                if (this.getMenusId().sameValueAs(menus.getParentId()) && menus.isButton() && menus.isEnable()) {
                    sb.append(menus.getCode()).append(",");
                }
            });
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 1);
            }
            this.code = sb.toString();
        }
    }

    /**
     * 设置菜单树
     *
     * @author Johnson.Jia
     * @date 2023/3/16 15:33:00
     * @param list
     * @return
     */
    public void addMenusTree(List<Menus> list) {
        if (CollectionUtils.isEmpty(this.list)) {
            this.list = list;
        } else {
            this.list.addAll(list);
        }
    }

    /**
     * 是否为菜单
     *
     * @author Johnson.Jia
     * @date 2023/3/16 14:53:16
     * @return true 菜单，false 功能、按钮
     */
    public boolean isMenus() {
        return this.type != null && this.type == MenusTypeEnum.MENUS;
    }

    /**
     * 是否为按钮、功能
     *
     * @author Johnson.Jia
     * @date 2023/3/16 14:59:56
     * @return
     */
    public boolean isButton() {
        return !isMenus();
    }

    /**
     * 菜单是否 已启用
     *
     * @author Johnson.Jia
     * @date 2023/3/16 14:50:31
     * @return true 启用，false 禁用
     */
    public boolean isEnable() {
        return this.status != null && this.status == MenusStatusEnum.ENABLE;
    }

    /**
     * 禁用菜单
     */
    public void disableMenus() {
        this.status = MenusStatusEnum.DISABLE;
    }

    /**
     * 启用菜单
     */
    public void enableMenus() {
        this.status = MenusStatusEnum.ENABLE;
    }

}
