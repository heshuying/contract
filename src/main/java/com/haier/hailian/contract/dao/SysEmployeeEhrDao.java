package com.haier.hailian.contract.dao;

import com.haier.hailian.contract.entity.SysEmployeeEhr;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 19012964
 * @since 2019-12-16
 */
public interface SysEmployeeEhrDao extends BaseMapper<SysEmployeeEhr> {

    String selectXwCode(@Param(value = "empSn") String empSn);

}
