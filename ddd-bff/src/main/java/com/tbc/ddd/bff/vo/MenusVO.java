package com.tbc.ddd.bff.vo;

import com.tbc.ddd.domain.role.enums.MenusStatusEnum;
import com.tbc.ddd.domain.role.enums.MenusTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单 VO
 *
 * @author Johnson.Jia
 */
@Data
@Builder
public class MenusVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String url;
    private String interfaceUrl;
    private String icon;
    private String reIcon;
    private Integer parentId;
    private MenusTypeEnum type;
    private String code;
    private MenusStatusEnum status;
    private List<MenusVO> list;
}
