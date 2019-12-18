package com.haier.hailian.contract.service;

import com.haier.hailian.contract.dto.grab.CDGrabInfoRequestDto;

import java.util.Map;

/**
 * @author 19033323
 */
public interface CDGrabService {
    Map<String, Object> queryCDGrabInfo(CDGrabInfoRequestDto requestDto);
}
