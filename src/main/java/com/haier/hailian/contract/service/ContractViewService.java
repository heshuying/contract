package com.haier.hailian.contract.service;

import com.haier.hailian.contract.dto.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ContractViewService {
    ContractViewResultDTO getContractViewData(String contractId);

    Map<String, List<ContractViewDataTY>> getContractViewDataTYOld(String contractId);

    List<ContractViewDataTYResultDTO> getContractViewDataTY(String contractId, String orderType);

    List<TargetTitleTYDTO> getTargetTitleList(String contractId);

    Collection<ContractViewDataCDResponseDTO> getContractViewDataCD(String contractId);

    String getContractSize(String contractId);

    Integer getContractSize2(String contractId);
}
