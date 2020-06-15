package com.haier.hailian.contract.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haier.hailian.contract.config.DingDingConfig;
import com.haier.hailian.contract.dto.RException;
import com.haier.hailian.contract.service.DingDingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 19012964 on 2020/6/11.
 */
@Service
@Slf4j
public class DingDingServiceImpl implements DingDingService{

    @Autowired
    private DingDingConfig dingDingConfig;
    @Autowired
    private RestTemplate nRestTemplate;

    @Override
    public String getAccessToken() {
        String method="/gateway/ihaier/gettoken?appkey={appkey}&appsecret={appsecret}";
        String uri=dingDingConfig.getBaseUri().concat(method);
        Map<String, String> map = new HashMap<>();
        map.put("appkey",dingDingConfig.getAppKey());
        map.put("appsecret", dingDingConfig.getAppSecret());
        ResponseEntity<String> responseEntity = nRestTemplate.getForEntity(uri,String.class,map);
        String body=responseEntity.getBody();
        log.info("=====获取accessToken：{}==",body);
        JSONObject jsonObject= JSON.parseObject(body);
        if(jsonObject.containsKey("errcode")&&"0".equals(jsonObject.getString("errcode"))){
            return jsonObject.getString("access_token");
        }else{
            throw new RException("获取accessToken失败");
        }

    }

    @Override
    public String getUserIdByToken(String code) {
        String method="/gateway/ihaier/user/getuserinfo?access_token={accessToken}&code={code}";
        String uri=dingDingConfig.getBaseUri().concat(method);
        String accessToken=getAccessToken();
        Map<String, String> map = new HashMap<>();
        map.put("accessToken",accessToken);
        map.put("code", code);
        ResponseEntity<String> responseEntity = nRestTemplate.getForEntity(uri,String.class,map);
        String body=responseEntity.getBody();
        log.info("=====获取用户ID返回：{}==",body);
        JSONObject jsonObject= JSON.parseObject(body);
        if(jsonObject.containsKey("errcode")&&"0".equals(jsonObject.getString("errcode"))){
            String userId= jsonObject.getString("userid");
            return this.getJobNumber(accessToken, userId);
        }else{
            throw new RException("登陆失败");
        }

    }

    private String getJobNumber(String accessToken, String userId) {
        String method="user/get?access_token={accessToken}&userid={userId}";
        String uri=dingDingConfig.getBaseUri().concat(method);
        Map<String, String> map = new HashMap<>();
        map.put("accessToken",accessToken);
        map.put("userId", userId);
        ResponseEntity<String> responseEntity = nRestTemplate.getForEntity(uri,String.class,map);
        String body=responseEntity.getBody();
        log.info("=====获取用户工号返回：{}==",body);
        JSONObject jsonObject= JSON.parseObject(body);
        if(jsonObject.containsKey("errcode")&&"0".equals(jsonObject.getString("errcode"))){
            return jsonObject.getString("jobnumber");
        }else{
            throw new RException("登陆失败");
        }

    }

    @Override
    public String createGroup(String lqName , String chainMasterCode , String[] users) {
        String method="/chat/create?access_token=" + getAccessToken();
        String uri=dingDingConfig.getBaseUri().concat(method);
        Map<String, Object> map = new HashMap<>();
        map.put("name",lqName + "链群交互群");
        map.put("owner", chainMasterCode);
        map.put("useridlist" , users);//创建时只传链群主自己
        ResponseEntity<String> responseEntity = nRestTemplate.postForEntity(uri,map,String.class);
        String body = responseEntity.getBody();
        log.info("=====创建群组返回：{}==",body);
        JSONObject jsonObject= JSON.parseObject(body);
        if(jsonObject.containsKey("errcode")&&"0".equals(jsonObject.getString("errcode"))){
            return jsonObject.getString("chatid");
        }else{
            throw new RException("创建群组失败");
        }
    }

    @Override
    public void addGroup(String groupId , String[] users , String updateType) {
        String method="/chat/update?access_token=" + getAccessToken();
        String uri=dingDingConfig.getBaseUri().concat(method);
        Map<String, Object> map = new HashMap<>();
        map.put("chatid",groupId);
        // 新增：add_useridlist； 删除：del_useridlist
        map.put(updateType , users);//群组新增或者删除
        ResponseEntity<String> responseEntity = nRestTemplate.postForEntity(uri,map,String.class);
        String body = responseEntity.getBody();
        log.info("=====修改群组返回：{}==",body);
        JSONObject jsonObject= JSON.parseObject(body);
        if(jsonObject.containsKey("errcode")&&"0".equals(jsonObject.getString("errcode"))){
            // 成功
        }else{
            throw new RException("修改群组失败");
        }
    }
}
