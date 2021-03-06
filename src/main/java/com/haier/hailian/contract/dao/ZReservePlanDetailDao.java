package com.haier.hailian.contract.dao;

import com.haier.hailian.contract.entity.ZReservePlanDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 19012964
 * @since 2019-12-19
 */
public interface ZReservePlanDetailDao extends BaseMapper<ZReservePlanDetail> {
    List<ZReservePlanDetail> getByContract(Integer contractId);
}
