package com.haier.hailian.contract.service;

import com.haier.hailian.contract.dto.grab.CDGrabInfoSaveRequestDto;
import java.math.BigDecimal;


public interface IncrementService {

    /**
     * 计算增值分享金额
     * @param requestDto
     * @return
     */
    BigDecimal incrementMoney(CDGrabInfoSaveRequestDto requestDto);

    BigDecimal incrementMoneyShareModifyOld(CDGrabInfoSaveRequestDto requestDto);

    BigDecimal incrementMoneyShareModify(CDGrabInfoSaveRequestDto requestDto);
}
