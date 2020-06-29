package com.haier.hailian.contract.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.haier.hailian.contract.config.DingDingConfig;
import com.haier.hailian.contract.dto.DingDingUserInfo;
import com.haier.hailian.contract.dto.RException;
import com.haier.hailian.contract.service.DingDingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
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

        String method="/gateway/ihaier/chat/create?access_token=" + getAccessToken();
        String uri=dingDingConfig.getBaseUri().concat(method);
        JSONObject json = new JSONObject();
        json.put("name",lqName + "链群交互群");
        json.put("owner", chainMasterCode);
        json.put("useridlist" , users);//创建时只传链群主自己

        HttpPost httpPost = new HttpPost(uri);
        RequestConfig requestConfig = RequestConfig.custom().
                setConnectTimeout(180 * 1000).setConnectionRequestTimeout(180 * 1000)
                .setSocketTimeout(180 * 1000).setRedirectsEnabled(true).build();
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("Content-Type", "application/json");

        org.json.simple.JSONObject jsonObject = doPostHeaderForJson(json.toJSONString() , httpPost);

        log.info("=====创建群组返回：{}==",jsonObject.toJSONString());
//        JSONObject jsonObject= JSON.parseObject(body);
        if(jsonObject.containsKey("errcode")&&"0".equals(jsonObject.get("errcode").toString())){
            return jsonObject.get("chatid").toString();
        }else{
            throw new RException("创建群组失败");
        }
    }

    @Override
    public void updateGroup(String groupId , String[] users , String updateType) {
        String method="/gateway/ihaier/chat/update?access_token=" + getAccessToken();
        String uri=dingDingConfig.getBaseUri().concat(method);

        JSONObject json = new JSONObject();
        json.put("chatid",groupId);
        // 新增：add_useridlist； 删除：del_useridlist
        json.put(updateType , users);//群组新增或者删除

        HttpPost httpPost = new HttpPost(uri);
        RequestConfig requestConfig = RequestConfig.custom().
                setConnectTimeout(180 * 1000).setConnectionRequestTimeout(180 * 1000)
                .setSocketTimeout(180 * 1000).setRedirectsEnabled(true).build();
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("Content-Type", "application/json");

        org.json.simple.JSONObject jsonObject = doPostHeaderForJson(json.toJSONString() , httpPost);

        log.info("=====修改群组返回：{}==",jsonObject.toJSONString());
        if(jsonObject.containsKey("errcode")&&"0".equals(jsonObject.get("errcode").toString())){
            // 成功
        }else{
            throw new RException("修改群组失败");
        }
    }


    public String getPhonebookToken(){
        Gson gson = new Gson();
        String method="/auth/oauth/token?username=S01800&password=y%2BxebMTkbcWhyUwGhKohhQ%3D%3D&grant_type=password&scope=server&step=1";
        String uri=dingDingConfig.getBaseUri().concat(method);
        // 获取token

        HttpPost httpPost = new HttpPost(uri);
        RequestConfig requestConfig = RequestConfig.custom().
                setConnectTimeout(180 * 1000).setConnectionRequestTimeout(180 * 1000)
                .setSocketTimeout(180 * 1000).setRedirectsEnabled(true).build();
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Authorization","Basic UzAxODAwOlMwMTgwMA==");
        httpPost.setHeader("TENANT_ID","1");

        org.json.simple.JSONObject jsonObject = doPostHeaderForJson("" , httpPost);

        if(!"1".equals(jsonObject.get("code"))){
            // 成功
            return jsonObject.get("access_token").toString();
        }else{
            throw new RException("获取通讯录token失败");
        }

    }


    @Override
    public String getUserId(String empNo) {
        String method="/admin/user/getDingUserId/" + empNo;
        String uri=dingDingConfig.getBaseUri().concat(method);

        // 获取userId

        HttpGet httpGet = new HttpGet(uri);
        RequestConfig requestConfig = RequestConfig.custom().
                setConnectTimeout(180 * 1000).setConnectionRequestTimeout(180 * 1000)
                .setSocketTimeout(180 * 1000).setRedirectsEnabled(true).build();
        httpGet.setConfig(requestConfig);
        httpGet.setHeader("Content-Type", "application/json");
        httpGet.setHeader("Authorization","Bearer " + getPhonebookToken());

        org.json.simple.JSONObject jsonObject = doGetHeaderForJson(httpGet);

        log.info("=====获取用户userId返回：{}==",jsonObject.toJSONString());
        if("0".equals(jsonObject.get("code").toString())){
            return jsonObject.get("data").toString();
        }else{
            throw new RException("获取userId失败");
        }

    }


    public static org.json.simple.JSONObject doPostHeaderForJson(String jsonParams , HttpPost httpPost) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        org.json.simple.JSONObject jsonObject = null;

        try {
            httpPost.setEntity(new StringEntity(jsonParams, ContentType.create("application/json", "utf-8")));
            log.info("request parameters={}", EntityUtils.toString(httpPost.getEntity()));
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity());
                log.info("post utils returns ={}", result);
                jsonObject = (org.json.simple.JSONObject)(new JSONParser().parse(result));
                return jsonObject;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return jsonObject;
        }
    }




    public static org.json.simple.JSONObject doGetHeaderForJson(HttpGet httpGet) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        org.json.simple.JSONObject jsonObject = null;

        try {
            HttpResponse response = httpClient.execute(httpGet);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity());
                log.info("get utils returns ={}", result);
                jsonObject = (org.json.simple.JSONObject)(new JSONParser().parse(result));
                return jsonObject;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return jsonObject;
        }
    }


}
