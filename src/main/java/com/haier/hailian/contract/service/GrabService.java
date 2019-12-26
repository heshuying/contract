package com.haier.hailian.contract.service;

import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.grab.TyMasterGrabQueryDto;
import com.haier.hailian.contract.dto.grab.MeshSummaryDto;
import com.haier.hailian.contract.dto.grab.MessGambSubmitDto;
import com.haier.hailian.contract.dto.grab.TyMasterGrabChainInfoDto;

/**
 * Created by 19012964 on 2019/12/17.
 */
public interface GrabService {
    TyMasterGrabChainInfoDto queryChainInfo(TyMasterGrabQueryDto queryDto);
    MeshSummaryDto queryMeshGrabDetail(TyMasterGrabQueryDto queryDto);

    R doGrab(MessGambSubmitDto dto);

    /**
     * 定时任务刷新合约状态，只刷类型为10的单
     */
    void refreshContractStatusJob();
}
