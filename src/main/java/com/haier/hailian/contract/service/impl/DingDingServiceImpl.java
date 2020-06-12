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
        Map<String, String> map = new HashMap<>();
        map.put("accessToken",getAccessToken());
        map.put("code", code);
        ResponseEntity<String> responseEntity = nRestTemplate.getForEntity(uri,String.class,map);
        String body=responseEntity.getBody();
        log.info("=====获取用户ID返回：{}==",body);
        JSONObject jsonObject= JSON.parseObject(body);
        if(jsonObject.containsKey("errcode")&&"0".equals(jsonObject.getString("errcode"))){
            return jsonObject.getString("userid");
        }else{
            throw new RException("登陆失败");
        }

    }
}
