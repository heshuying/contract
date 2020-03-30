package com.haier.hailian.contract.service;

import com.haier.hailian.contract.dto.FactorGrabResDTO;
import com.haier.hailian.contract.dto.QueryContractListDTO;
import com.haier.hailian.contract.dto.TargetListResDTO;
import com.haier.hailian.contract.dto.TargetReachSaveReqDTO;
import com.haier.hailian.contract.entity.ZContracts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface TargetReachService {
    List<ZContracts> selectContractListForTarget(QueryContractListDTO queryDTO);

    List<TargetListResDTO> getFactorGrabList(String contractId);

    void saveTargetActual(TargetReachSaveReqDTO data);

    void templetDownload(String contractId, HttpServletRequest request, HttpServletResponse response, boolean isTemplet) throws IOException;

    List<TargetListResDTO> getDataByExcel(InputStream inputStream, String fileName) throws Exception;
}
