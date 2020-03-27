package com.haier.hailian.contract.service;

import com.haier.hailian.contract.dto.FactorGrabResDTO;
import com.haier.hailian.contract.dto.QueryContractListDTO;
import com.haier.hailian.contract.dto.TargetListResDTO;
import com.haier.hailian.contract.dto.TargetReachSaveReqDTO;
import com.haier.hailian.contract.entity.ZContracts;

import java.util.List;

public interface TargetReachService {
    List<ZContracts> selectContractListForTarget(QueryContractListDTO queryDTO);

    List<TargetListResDTO> getFactorGrabList(String contractId);

    void saveTargetActual(TargetReachSaveReqDTO data);
}
