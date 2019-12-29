package com.haier.hailian.contract.service;

import com.haier.hailian.contract.dto.ContractViewDataCD;
import com.haier.hailian.contract.dto.ContractViewResultDTO;
import com.haier.hailian.contract.entity.ZContractsFactor;

import java.util.List;
import java.util.Map;

public interface ContractViewService {
    ContractViewResultDTO getContractViewData(String contractId);

    Map<String, List<ZContractsFactor>> getContractViewDataTY(String contractId);

    List<ContractViewDataCD> getContractViewDataCD(String contractId);
}
