package com.haier.hailian.contract.service;

import com.haier.hailian.contract.dto.grab.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author 19033323
 */
public interface CDGrabService {
    CDGrabInfoResponseDto queryCDGrabInfo(CDGrabInfoRequestDto requestDto);

    CDGrabViewResponseDto queryCDGrabView(CDGrabInfoRequestDto requestDto);

    List<CDGrabHistoryResponseDto> queryCDGrabHistoryView(CDGrabInfoRequestDto requestDto);

    @Transactional
    void saveCDGrab(CDGrabInfoSaveRequestDto requestDto);


    void test(String contractId);
    @Transactional
    void updateCDGrab(CDGrabInfoSaveRequestDto requestDto);

    Integer updateCancelGrab(String contractId);

    Integer updateKickOff(String contractId);
}
