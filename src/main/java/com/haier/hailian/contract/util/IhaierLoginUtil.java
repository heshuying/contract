package com.haier.hailian.contract.util;


import org.json.simple.JSONObject;

/**
 * Created by 01466498 on 2019/7/4.
 */
public class IhaierLoginUtil {
    /**********************************ihaier单点登录开始************************************/
    //获取token接口url
    static String accessTokenUrl = "https://i.haier.net/gateway/oauth2/token/getAccessToken";
    static String appid = "500000237";
    static String secret = "6MngO9QxehEhkMjxGPVB";
    static String scope = "app";
    //获取用户上下文接口url
    static String gatewayUrl= "https://i.haier.net/gateway/ticket/user/acquirecontext?accessToken=";

    public static String getAccessToken(){
        String accessToken = "";
        try{
            long timestamp = System.currentTimeMillis();
            String paramJson = "{" +
                    "    \"appId\": \""+appid+"\"," +
                    "    \"secret\": \""+secret+"\"," +
                    "    \"timestamp\": "+timestamp+"," +
                    "    \"scope\": \""+scope+"\"" +
                    "}";
            JSONObject json = HttpContextUtils.doPostForJson(accessTokenUrl,paramJson);
            String status = json.get("success").toString();
            if("true".equals(status)){
                JSONObject data = (JSONObject)json.get("data");
                accessToken = data.get("accessToken").toString();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return accessToken;
    }
    public static String getUser(String ticket,String accessToken){
        String jobNo = "";
        try{
            String url = gatewayUrl + accessToken;
            String paramJson = "{" +
                    "    \"appid\":\""+appid+"\"," +
                    "    \"ticket\":\""+ticket+"\"" +
                    "}";
            JSONObject json = HttpContextUtils.doPostForJson(url,paramJson);
            String status = json.get("success").toString();
            if("true".equals(status)){
                JSONObject data = (JSONObject)json.get("data");
                jobNo = data.get("jobNo").toString();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return jobNo;
    }
    /**********************************ihaier单点登录结束************************************/

    /**********************************ihaier头像获取开始************************************/
    static String headAccessTokenUrl = "http://apigw.haier.net/getopenid/gateway/oauth2/token/getAccessToken";
    static String getUserUrl  = "https://i.haier.net/gateway/openimport/open/person/get?accessToken=";
    public static String getTouxiangAccessToken(){
        String accessToken = "";
        try{
            long timestamp = System.currentTimeMillis();
            String paramJson = "{" +
                    "    \"timestamp\": "+timestamp+"" +
                    "}";
            JSONObject json = HttpContextUtils.doPostHeaderForJson(headAccessTokenUrl,paramJson);
            String status = json.get("success").toString();
            if("true".equals(status)){
                JSONObject data = (JSONObject)json.get("data");
                accessToken = data.get("accessToken").toString();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return accessToken;
    }
    public static String getTouXiangUser(String accessToken){
        String jobNo = "";
        try{
            String url = getUserUrl + accessToken;
            String paramJson = "{" +
                    "\"eid\":\"102\"," +
                    "\"data\":{\"array\": [\"01466498@haier.com\"]}," +
                    "\"type\":0" +
                    "}";
            JSONObject json = HttpContextUtils.doPostForJson(url,paramJson);
            String status = json.get("success").toString();
            if("true".equals(status)){
                JSONObject data = (JSONObject)json.get("data");
                jobNo = data.get("jobNo").toString();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return jobNo;
    }

    /**********************************ihaier头像获取结束************************************/
    public static void main(String[] args){
       String token  =  getTouxiangAccessToken();
        System.out.println("@@@@@@@@@@@@@2"+token);
        getTouXiangUser(token);
        //getUser("");
    }
}
