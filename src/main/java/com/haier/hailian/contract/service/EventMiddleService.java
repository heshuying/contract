package com.haier.hailian.contract.service;

import com.haier.hailian.contract.dto.*;
import com.haier.hailian.contract.entity.ZContractsFactor;

import java.util.List;

/**
 * Created by 01431594 on 2020/4/10.
 */
public interface EventMiddleService {
    List<ZContractsFactor> selectChainTarget(EventMiddleDTO dto);

    List<EventMiddleTYDTO> selectTyTarget(EventMiddleDTO dto);

    List<EventMiddleCdDTO> selectCdTarget(EventMiddleDTO dto);

    List<ContractProductDTO> selectProductTarget(EventMiddleDTO dto);

    List<EventMiddleTrendDTO> selectChainTargetTrend(EventMiddleDTO dto);

    EventMiddleChainShareDTO selectChainShare(EventMiddleDTO dto);

    List<EventMiddleTrendDTO> selectChainShareTrend(EventMiddleDTO dto);

    List<EventMiddleCdDTO> selectCdShare(EventMiddleDTO dto);
}
