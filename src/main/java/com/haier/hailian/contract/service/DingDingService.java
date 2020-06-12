package com.haier.hailian.contract.service;

import org.springframework.scheduling.annotation.Async;

/**
 * Created by 19012964 on 2020/6/11.
 */
public interface DingDingService {
    public String getAccessToken();

    public String getUserIdByToken(String code);

    public String createGroup(String lqName , String chainMasterCode , String[] users);

    @Async
    public void addGroup(String groupId , String[] users , String updateType);
}
