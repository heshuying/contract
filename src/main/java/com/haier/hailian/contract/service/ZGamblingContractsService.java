package com.haier.hailian.contract.service;

import com.haier.hailian.contract.dto.GamblingContractDTO;
import com.haier.hailian.contract.dto.MarketReturnDTO;
import com.haier.hailian.contract.dto.QueryContractListDTO;
import com.haier.hailian.contract.dto.QueryProductChainDTO;
import com.haier.hailian.contract.entity.ZContracts;
import com.haier.hailian.contract.entity.ZProductChain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 01431594
 * @since 2019-12-19
 */
public interface ZGamblingContractsService {

    void saveGambling(GamblingContractDTO dto) throws Exception;

    MarketReturnDTO selectMarket();

    List<ZContracts> selectContractList(QueryContractListDTO queryDTO);

    List<ZContracts> selectMyStartContract(QueryContractListDTO queryDTO);

    List<ZContracts> selectMyGrabContract(QueryContractListDTO queryDTO);

    List<ZContracts> selectToGrabContract(QueryContractListDTO queryDTO);

    List<ZProductChain> selectProductSeries(QueryProductChainDTO dto);

    GamblingContractDTO selectContractById(Integer contractId);

    void exportMarket(HttpServletRequest request, HttpServletResponse response) throws IOException;

}
