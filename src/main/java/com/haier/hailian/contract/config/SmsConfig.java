package com.haier.hailian.contract.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by 19012964 on 2020/3/11.
 */
@Data
@Component
@ConfigurationProperties(prefix = "sms-conf")
public class SmsConfig {
    private String smsServer;
    private String sysCode;
    private String sendTime;
    private String tokenKey;
    private int smsType;
    private int sendType;
    private Boolean flag;
}
