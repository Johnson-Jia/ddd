package com.tbc.ddd.common.ddd;

import java.io.Serializable;
import java.util.List;

/**
 * 基础仓储接口 标记
 *
 * @author Johnson.Jia
 */
public interface Repository<AGGREGATE, ID extends Serializable> {

    /**
     * 根据主键id删除
     *
     * @param id
     */
    void deleteById(ID id);

    /**
     * 根据主键id查找
     *
     * @param id
     * @return
     */
    AGGREGATE getById(ID id);

    /**
     * 保存
     *
     * @param aggregate
     * @param <S>
     * @return
     */
    <S extends AGGREGATE> S save(S aggregate);

    /**
     * 更新聚合根
     *
     * @author Johnson.Jia
     * @param aggregate
     * @return
     */
    <S extends AGGREGATE> S update(S aggregate);

    /**
     * 查询列表
     *
     * @author Johnson.Jia
     * @date 2023/3/21 11:08:23
     * @param ids
     *            主建集合
     * @return
     */
    List<AGGREGATE> getListByIds(List<ID> ids);

}
