package com.haier.hailian.contract.service;


import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.sms.PhoneMessage;
import com.haier.hailian.contract.dto.sms.SendMsgDto;
import com.haier.hailian.contract.entity.SysMsg;

import java.util.List;

/**
 * <p>
 * ${desc}
 * </p>
 *
 * @author sunjian 01505617
 * @since 2019/5/16
 */
public interface SendMessageService {
    /**
     * 根据短信模版生成短信内容
     * @param messagePattern
     * @param argArray
     * @return
     */
    String arrayFormat(String messagePattern, String... argArray);

    /**
     * 发送短信验证码
     *
     * @param phoneMessages
     * @return
     */
    R sendValidCode(SendMsgDto phoneMessages);

    /**
     * 批量发送短信
     *
     * @param list
     * @return
     */
    Boolean sendSMS(List<SysMsg> list);

    /**
     * 通知下游认领目标
     * @return
     */
    Boolean sendSubUser();


    /**
     * 发送短信验证码
     * @param sendSMSCodeDTO
     * @param request
     * @return
     */
//    Boolean sendSMSCode(SendSMSCodeDTO sendSMSCodeDTO, HttpServletRequest request);


}
