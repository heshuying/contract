package com.haier.hailian.contract.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by 19012964 on 2020/3/11.
 */
@Data
@Component
@ConfigurationProperties(prefix = "dingding")
public class DingDingConfig {
    private String AppKey;
    private String AppSecret;
    private String CorpId;
    private String baseUri;
}
