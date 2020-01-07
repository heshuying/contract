package com.haier.hailian.contract.service;

import com.haier.hailian.contract.dto.grab.CDGrabInfoRequestDto;
import com.haier.hailian.contract.dto.grab.CDGrabInfoResponseDto;
import com.haier.hailian.contract.dto.grab.CDGrabInfoSaveRequestDto;
import com.haier.hailian.contract.dto.grab.CDGrabViewResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author 19033323
 */
public interface CDGrabService {
    CDGrabInfoResponseDto queryCDGrabInfo(CDGrabInfoRequestDto requestDto);

    CDGrabViewResponseDto queryCDGrabView(CDGrabInfoRequestDto requestDto);

    @Transactional
    void saveCDGrab(CDGrabInfoSaveRequestDto requestDto);

    Integer updateCancelGrab(String contractId);
}
