package com.haier.hailian.contract.service.impl;

import com.haier.hailian.contract.dto.grab.CDGrabInfoRequestDto;
import com.haier.hailian.contract.service.CDGrabService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

/**
 * @author 19033323
 */
@Service
public class CDGrabServiceImpl implements CDGrabService {

    @Override
    public Map<String, Object> queryCDGrabInfo(CDGrabInfoRequestDto requestDto){
        return Collections.EMPTY_MAP;
    }
}
