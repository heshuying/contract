package com.haier.hailian.contract.service;

import com.haier.hailian.contract.dto.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ContractViewService {
    ContractViewResultDTO getContractViewData(String contractId);

    Map<String, List<ContractViewDataTY>> getContractViewDataTYOld(String contractId);

    List<ContractViewDataTYResultDTO> getContractViewDataTY(String contractId, String orderType);

    Map<String, Object> getContractViewDataTYNew(ContractViewRequestNewDTO requestBean);

    int selectContractsViewForTYCount(String contractId);

    List<TargetTitleTYDTO> getTargetTitleList(String contractId);

    Collection<ContractViewDataCDResponseDTO> getContractViewDataCD(String contractId, String xwName);

    List<CDGrabType3> queryCDGrabDataXWType3(String contractId);

    List<CDGrabDataDTO> queryGrabListXWType3(String contractId, String type3Code);

    int updateCDSharePercent(String contractId, String sharePercent);

    String getContractSize(String contractId);

    Integer getContractSize2(String contractId);

    List<ContractSerialDto> staticSerial(Integer contractId);

    void exportContract(Integer contractId);
}
