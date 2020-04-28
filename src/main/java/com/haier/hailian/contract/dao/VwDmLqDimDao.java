package com.haier.hailian.contract.dao;

import com.haier.hailian.contract.entity.VwDmLqDim;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 19033715
 * @since 2020-04-24
 */
public interface VwDmLqDimDao extends BaseMapper<VwDmLqDim> {


    List<VwDmLqDim> selectChainList(Map<String, Object> map);

}
