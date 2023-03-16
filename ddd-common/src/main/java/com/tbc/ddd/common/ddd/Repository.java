package com.tbc.ddd.common.ddd;

import java.io.Serializable;

/**
 * 基础仓储接口
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
     * 保存或更新聚合根
     *
     * @param aggregate
     * @param <S>
     * @return
     */
    <S extends AGGREGATE> S save(S aggregate);

}
