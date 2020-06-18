package com.haier.hailian.contract.service;

import com.haier.hailian.contract.dto.DingDingUserInfo;
import org.springframework.scheduling.annotation.Async;

/**
 * Created by 19012964 on 2020/6/11.
 */
public interface DingDingService {
    String getAccessToken();

    DingDingUserInfo getUserIdByToken(String code);

    String createGroup(String lqName , String chainMasterCode , String[] users);

    @Async
    void updateGroup(String groupId , String[] users , String updateType);

    String getUserId(String empNo);

}
