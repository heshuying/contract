package com.haier.hailian.contract.dao;

import com.haier.hailian.contract.entity.SysEmployeeEhr;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haier.hailian.contract.entity.SysNodeEhr;
import com.haier.hailian.contract.entity.SysRole;
import com.haier.hailian.contract.entity.SysXiaoweiEhr;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
    List<SysRole> selectRoleByUser(@Param(value = "empSn") String empSn);
    List<SysNodeEhr> selectNodeByNodeCode(@Param(value = "empSn") String empSn);
    List<SysXiaoweiEhr> selectXiaoweiByEmpId(@Param(value = "empSn") String empSn);

    SysEmployeeEhr selectInfo(@Param(value = "empSn") String empSn);
}
