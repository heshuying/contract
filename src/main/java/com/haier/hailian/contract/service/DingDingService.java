package com.haier.hailian.contract.service;

/**
 * Created by 19012964 on 2020/6/11.
 */
public interface DingDingService {
    public String getAccessToken();

    public String getUserIdByToken(String code);
}
