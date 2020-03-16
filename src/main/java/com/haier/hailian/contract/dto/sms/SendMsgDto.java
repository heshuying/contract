package com.haier.hailian.contract.dto.sms;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by 19012964 on 2020/3/11.
 */
@Data
public class SendMsgDto {
    @NotBlank(message = "手机号不可以为空！")
    private String cellphone;
    @NotBlank(message = "业务类型不可以为空！")
    private String bizType;
    private String validCode;
}
