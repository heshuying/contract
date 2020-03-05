package com.haier.hailian.contract.service;

import com.haier.hailian.contract.entity.ZContracts;
import com.haier.hailian.contract.entity.ZContractsFactor;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * Created by 19012964 on 2019/12/24.
 */
public interface ChainCommonService {
    String uploadJsonData(String json);
    void doChainAfterGrab(ZContracts contract, String dataHash);

    void buildContractChain(Integer contractId);

    void getContractFromChain(Integer contractId);

    void uploadBigContract(Integer contractId);

    void doChain(String json);
}
