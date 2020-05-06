package com.haier.hailian.contract.dao;

import com.haier.hailian.contract.entity.CdJd;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 节点基础表-来源ODS-最小作战单元 Mapper 接口
 * </p>
 *
 * @author 19033715
 * @since 2020-05-06
 */
public interface CdJdDao extends BaseMapper<CdJd> {

    List<CdJd> selectGrabStarMap(Map<String, Object> map);

}
