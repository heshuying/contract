package com.haier.hailian.contract.service;

/**
 * Created by 19012964 on 2019/12/24.
 */
public interface ChainCommonService {
    String uploadJsonData(String json);
    void doChainAfterGrab(String contractId, Integer status, String dataHash);
}
