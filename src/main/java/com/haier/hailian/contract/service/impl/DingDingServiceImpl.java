package com.haier.hailian.contract.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haier.hailian.contract.config.DingDingConfig;
import com.haier.hailian.contract.dto.DingDingUserInfo;
import com.haier.hailian.contract.dto.RException;
import com.haier.hailian.contract.dto.sms.NotifySms;
import com.haier.hailian.contract.service.DingDingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
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
    public DingDingUserInfo getUserIdByToken(String code) {
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

    private DingDingUserInfo getJobNumber(String accessToken, String userId) {
        String method="/gateway/ihaier/user/get?access_token={accessToken}&userid={userId}";
        String uri=dingDingConfig.getBaseUri().concat(method);
        Map<String, String> map = new HashMap<>();
        map.put("accessToken",accessToken);
        map.put("userId", userId);
        ResponseEntity<String> responseEntity = nRestTemplate.getForEntity(uri,String.class,map);
        String body=responseEntity.getBody();
        log.info("=====获取用户工号返回：{}==",body);
        JSONObject jsonObject= JSON.parseObject(body);
        if(jsonObject.containsKey("errcode")&&"0".equals(jsonObject.getString("errcode"))){
            DingDingUserInfo dingUser= JSONObject.toJavaObject(jsonObject, DingDingUserInfo.class);
            return dingUser;
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
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map);
        ResponseEntity<String> responseEntity = nRestTemplate.postForEntity(uri,entity,String.class);
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
    public void updateGroup(String groupId , String[] users , String updateType) {
        String method="/chat/update?access_token=" + getAccessToken();
        String uri=dingDingConfig.getBaseUri().concat(method);
        Map<String, Object> map = new HashMap<>();
        map.put("chatid",groupId);
        // 新增：add_useridlist； 删除：del_useridlist
        map.put(updateType , users);//群组新增或者删除
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map);
        ResponseEntity<String> responseEntity = nRestTemplate.postForEntity(uri,entity,String.class);
        String body = responseEntity.getBody();
        log.info("=====修改群组返回：{}==",body);
        JSONObject jsonObject= JSON.parseObject(body);
        if(jsonObject.containsKey("errcode")&&"0".equals(jsonObject.getString("errcode"))){
            // 成功
        }else{
            throw new RException("修改群组失败");
        }
    }


    public String getPhonebookToken(){
        String method="/auth/oauth/token";
        String uri=dingDingConfig.getBaseUri().concat(method);
        Map<String, Object> map = new HashMap<>();
        map.put("username","S01800");
        map.put("password","y%2BxebMTkbcWhyUwGhKohhQ%3D%3D");
        map.put("grant_type","password");
        map.put("scope","server");
        map.put("step","1");
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map);
        ResponseEntity<String> responseEntity = nRestTemplate.postForEntity(uri,entity,String.class);
        String body = responseEntity.getBody();
        log.info("=====修改群组返回：{}==",body);
        JSONObject jsonObject= JSON.parseObject(body);
        if(!"1".equals(jsonObject.getString("code"))){
            // 成功
            return jsonObject.getString("access_token");
        }else{
            throw new RException("获取通讯录token失败");
        }
    }


    @Override
    public String getUserId(String empNo) {
        String method="/admin/user/getDingUserId/" + empNo;
        String uri=dingDingConfig.getBaseUri().concat(method);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // TODO
        String basic=getPhonebookToken();
        headers.set("Authorization", "bearer "+ basic);

//        Map<String, String> map = new HashMap<>();
//        map.put("accessToken",getAccessToken());
        ResponseEntity<String> responseEntity = nRestTemplate.getForEntity(uri,String.class,headers);
        String body=responseEntity.getBody();
        log.info("=====获取用户userId返回：{}==",body);
        JSONObject jsonObject= JSON.parseObject(body);
        if(jsonObject.containsKey("code")&&"0".equals(jsonObject.getString("code"))){
            return jsonObject.getString("data");
        }else{
            throw new RException("获取失败");
        }

    }
}
