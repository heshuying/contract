package com.haier.hailian.contract.service;

import com.haier.hailian.contract.dto.FactorGrabResDTO;
import com.haier.hailian.contract.dto.TargetReachSaveReqDTO;

import java.util.List;

public interface TargetReachService {
    List<FactorGrabResDTO> getFactorGrabList(String contractId);

    void saveTargetActual(List<TargetReachSaveReqDTO> reqBean);
}
