package com.haier.hailian.contract.dto.sms;

import lombok.Data;

/**
 * Created by 19012964 on 2020/3/11.
 */
@Data
public class SendMsgDto {
    private String cellphone;
    private String bizType;
}
