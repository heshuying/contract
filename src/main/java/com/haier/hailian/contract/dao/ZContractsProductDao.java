package com.haier.hailian.contract.dao;

import com.haier.hailian.contract.dto.ContractProductDTO;
import com.haier.hailian.contract.entity.ProductQueryEntity;
import com.haier.hailian.contract.entity.ZContractsProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 01431594
 * @since 2020-01-08
 */
public interface ZContractsProductDao extends BaseMapper<ZContractsProduct> {
    List<ZContractsProduct> calContractProduct(ProductQueryEntity entity);
    List<ZContractsProduct> distinctSerialAndScene(Integer contractId);
    List<ContractProductDTO> selectProductByContractId(Integer contractId);
}
