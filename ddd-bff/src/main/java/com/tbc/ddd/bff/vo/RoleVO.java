package com.tbc.ddd.bff.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 角色 VO
 *
 * @author Johnson.Jia
 */
@Data
@Builder
public class RoleVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private List<MenusVO> list;
}
