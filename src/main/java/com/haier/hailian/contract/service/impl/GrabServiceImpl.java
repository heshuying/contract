package com.haier.hailian.contract.service.impl;

import com.haier.hailian.contract.dto.grab.MeshGrabInfoDto;
import com.haier.hailian.contract.dto.grab.MeshStatisticQueryDto;
import com.haier.hailian.contract.dto.grab.MeshSummaryDto;
import com.haier.hailian.contract.dto.grab.TyMasterGrabChainInfoDto;
import com.haier.hailian.contract.service.GrabService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 19012964 on 2019/12/17.
 */
@Service
public class GrabServiceImpl implements GrabService {
    @Override
    public TyMasterGrabChainInfoDto queryChainInfo(MeshStatisticQueryDto queryDto) {
        return null;
    }

    @Override
    public MeshSummaryDto queryMeshGrabDetail(MeshStatisticQueryDto queryDto) {
        return null;
    }
}
