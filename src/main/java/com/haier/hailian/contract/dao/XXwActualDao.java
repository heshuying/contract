package com.haier.hailian.contract.dao;

import com.haier.hailian.contract.dto.homepage.ChainGroupInfoDto;
import com.haier.hailian.contract.entity.XXwActual;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.math.BigDecimal;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuyq
 * @since 2019-12-19
 */
public interface XXwActualDao extends BaseMapper<XXwActual> {

    BigDecimal getChainFact(ChainGroupInfoDto chainGroupInfoDto);

}
