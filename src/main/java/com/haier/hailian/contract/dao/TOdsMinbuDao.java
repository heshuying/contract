package com.haier.hailian.contract.dao;

import com.haier.hailian.contract.entity.TOdsMinbu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 19012964
 * @since 2019-12-28
 */
public interface TOdsMinbuDao extends BaseMapper<TOdsMinbu> {
    List<TOdsMinbu> queryMinbuByEmp(String empSn);
}
