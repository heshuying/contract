package com.haier.hailian.contract.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  HAC登陆rest api配置信息
 * </p>
 *
 * @author 19012964
 * @since 2019/12/06
 */
@Data
@Component
@ConfigurationProperties(prefix = "hac")
public class HacLoginConfig {
    private String loginUri;
    private String appKey;
    private String appSecret;
}
