package com.haier.hailian.contract.service;

/**
 * Created by 19012964 on 2020/6/11.
 */
public interface DingDingService {
     String getAccessToken();

    String getUserIdByToken(String code);

    public String createGroup(String lqName , String chainMasterCode , String[] users);

    @Async
    public void addGroup(String groupId , String[] users , String updateType);

}
