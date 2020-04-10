package com.haier.hailian.contract.service;

import com.haier.hailian.contract.dto.EventMiddleDTO;
import com.haier.hailian.contract.entity.ZContractsFactor;

import java.util.List;

/**
 * Created by 01431594 on 2020/4/10.
 */
public interface EventMiddleService {
    List<ZContractsFactor> selectChainTarget(EventMiddleDTO dto);
}
