package com.haier.hailian.contract.service;

import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.grab.MeshGrabInfoDto;
import com.haier.hailian.contract.dto.grab.TyGrabListQueryDto;
import com.haier.hailian.contract.dto.grab.TyMasterGrabQueryDto;
import com.haier.hailian.contract.dto.grab.MessGambSubmitDto;
import com.haier.hailian.contract.dto.grab.TyMasterGrabChainInfoDto;

import java.util.List;

/**
 * Created by 19012964 on 2019/12/17.
 */
public interface GrabService {
    TyMasterGrabChainInfoDto queryChainInfo(TyMasterGrabQueryDto queryDto);
    List<MeshGrabInfoDto> queryMeshGrabDetail(TyMasterGrabQueryDto queryDto);

    R doGrab(MessGambSubmitDto dto);

    /**
     * 定时任务刷新合约状态，只刷类型为10的单
     */
    void refreshContractStatusJob();

    /**
     * 查询可抢入合约
     * @param queryDto
     * @return
     */
    List<TyMasterGrabChainInfoDto> queryChainList(TyGrabListQueryDto queryDto);

    /**
     * 查询已抢入合约
     * @param queryDto
     * @return
     */
    List<TyMasterGrabChainInfoDto> queryMyChainList(TyGrabListQueryDto queryDto);


    TyMasterGrabChainInfoDto queryChainInfo(Integer contractId);
    List<MeshGrabInfoDto> queryMeshGrabDetail(Integer contractId);
}
