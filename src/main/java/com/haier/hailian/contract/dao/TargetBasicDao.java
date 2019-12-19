package com.haier.hailian.contract.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haier.hailian.contract.entity.TargetBasic;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 01431594
 * @since 2019-12-18
 */
public interface TargetBasicDao extends BaseMapper<TargetBasic> {

    List<TargetBasic> selectTarget(TargetBasic targetBasic);
}
