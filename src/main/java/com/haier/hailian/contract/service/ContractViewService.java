package com.haier.hailian.contract.service;

import com.haier.hailian.contract.dto.*;
import com.haier.hailian.contract.entity.ZContracts;
import com.haier.hailian.contract.entity.ZContractsFactor;

import java.util.List;
import java.util.Map;

public interface ContractViewService {
    ContractViewResultDTO getContractViewData(String contractId);

    Map<String, List<ContractViewDataTY>> getContractViewDataTYOld(String contractId);

    List<ContractViewDataTYResultDTO> getContractViewDataTY(String contractId);

    List<TargetTitleTYDTO> getTargetTitleList(String contractId);

    List<ContractViewDataCD> getContractViewDataCD(String contractId);

    String getContractSize(String contractId);
}
