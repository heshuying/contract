package com.haier.hailian.contract.service;

import com.haier.hailian.contract.dto.*;
import com.haier.hailian.contract.entity.ZContracts;

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

    List<CDGrabType3> queryCDGrabDataXWType3(String contractId,String keyword);

    List<CDGrabDataDTO> queryGrabListXWType3(String contractId, String type3Code, String grabId);

    List<String> queryContractsForUpdate();

    void insertXWType3Count(String contractId, CDGrabType3 cdGrabType3);

    void updateCDSharePercent(String contractId, String sharePercent);

    void updateCDShareSpace(String contractId);

    void updateSharePercentChainMaster(String contractId);

    List<ZContracts> getContractForUpdateSPercent();

    String getContractSize(String contractId);

    Integer getContractSize2(String contractId);

    List<ContractProductDTO> staticSerial(Integer contractId);

    void exportContract(Integer contractId);
}
