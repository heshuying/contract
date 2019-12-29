package com.haier.hailian.contract.service;

import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.grab.MeshGrabInfoDto;
import com.haier.hailian.contract.dto.grab.TyMasterGrabQueryDto;
import com.haier.hailian.contract.dto.grab.MeshSummaryDto;
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

    List<TyMasterGrabChainInfoDto> queryChainList();
}
