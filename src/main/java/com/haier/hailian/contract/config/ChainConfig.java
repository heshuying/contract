package com.haier.hailian.contract.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  合约上链
 * </p>
 *
 * @author 19012964
 * @since 2019/12/06
 */
@Data
@Component
@ConfigurationProperties(prefix = "chain")
public class ChainConfig {
    private String uploadUrl;
    private String validUrl;
    private String apiGatewayAuthAppId;
    private String apiGatewayAuthAppPassword;

    private String contractUri;
    private String contractPrivateKey;
    private String contractPublicKeyHead;
    private String contractPublicKeyTail;
    private String contractAddress;
}
