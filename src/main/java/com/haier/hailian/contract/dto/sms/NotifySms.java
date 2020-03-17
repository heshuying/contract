package com.haier.hailian.contract.dto.sms;

import lombok.Data;

import java.util.List;

@Data
public class NotifySms {
    private String sysCode;
    private String sendTime;
    private String token;
    private int smsType;
    private int sendType;
    private List<NotifyMessage> messages;

}
