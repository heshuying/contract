package com.haier.hailian.contract.service;

import com.haier.hailian.contract.dto.*;
import com.haier.hailian.contract.entity.ZContracts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
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

    MarketReturnDTO selectMarket(String chainCode);

    List<ZContracts> selectContractList(QueryContractListDTO queryDTO);

    List<ZContracts> selectMyStartContract(QueryContractListDTO queryDTO);

    List<ZContracts> selectMyGrabContract(QueryContractListDTO queryDTO);

    List<ZContracts> selectToGrabContract(QueryContractListDTO queryDTO);

    List<ContractProductDTO> selectProductSeries(QueryProductChainDTO dto);

    GamblingContractDTO selectContractById(Integer contractId);

    void exportMarket(String chainCode,HttpServletRequest request, HttpServletResponse response) throws IOException;

    List<MarketTargetDTO> getMarketTargetListByExcel(InputStream inputStream, String originalFilename) throws Exception;

    List<ZContracts> selectHomePageContract(QueryContractListDTO2 queryDTO);

    void exportProductSeries(String chainCode, HttpServletRequest request, HttpServletResponse response) throws IOException;

    List<ContractProductDTO> getProductSeriesListByExcel(InputStream inputStream, String originalFilename)throws Exception;

    void saveGamblingNew(SaveGamblingContractDTO dto) throws Exception;

    List<ContractXwType3DTO> selectXwType3(String chainCode);
}
