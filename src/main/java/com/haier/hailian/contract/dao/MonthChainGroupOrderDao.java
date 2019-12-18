package com.haier.hailian.contract.dao;

import com.haier.hailian.contract.entity.MonthChainGroupOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 19012964
 * @since 2019-12-17
 */
public interface MonthChainGroupOrderDao extends BaseMapper<MonthChainGroupOrder> {

    /**
     * 根据合约和产品结构获取产品编码
     * @param contractId
     * @param productStru
     * @param yearMonth
     * @return
     */
    List<String> getProductByContract(@Param(value = "contractId") Integer contractId,
                                      @Param(value = "productStru") String productStru,
                                      @Param(value = "yearMonth") List<String> yearMonth);
}
