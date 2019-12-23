package com.haier.hailian.contract.dao;

import com.haier.hailian.contract.entity.SysEmpChain;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 19012964
 * @since 2019-12-23
 */
public interface SysEmpChainDao extends BaseMapper<SysEmpChain> {
    List<String> selectNoChainEmp();
}
