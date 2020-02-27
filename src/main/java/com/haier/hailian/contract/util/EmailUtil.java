package com.haier.hailian.contract.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * @author 01439613
 */
public class EmailUtil {

    public static void main(String[] args) {
        EmailUtil.transmitMsg("jiangzd102@outlook.com","测试我的邮件","测试邮件能否发送成功！");
    }
    public static String transmitMsg(String tos, String title, String content) {
        String status = "";
        PostMethod method = null;
        HttpClient client = new HttpClient();
        int statusCode = 0;
        client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        // 设置连接超时时间为30秒（连接初始化时间）
        client.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
        try {
            //对于特殊字符处理
            content = URLEncoder.encode(content, "utf-8");
            title = URLEncoder.encode(title, "utf-8");
            String senderName = "";
            String attachmentName = "";
            senderName = URLEncoder.encode(senderName, "utf-8");
            attachmentName = URLEncoder.encode(attachmentName, "utf-8");
            method = new PostMethod(
                    "http://his.haier.net:8989/services/smsemail/sendHisEmail");
            method.setParameter("tos", tos);
            method.setParameter("title", title);
            method.setParameter("content", content);
            method.setParameter("system", "OTCsys");
            method.setParameter("from", "iof@haier.com");
            method.setParameter("senderName", senderName);
            method.setParameter("Cc", "");
            method.setParameter("Bcc", "");
            method.setParameter("attachmentName", "");
            method.setParameter("attachmentBytes", "");
            method.setParameter("eventHisId", "");
            method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=utf-8");
            statusCode = client.executeMethod(method);

            // 读取内容
            byte[] responseBody = method.getResponseBody();
            // 处理内容
            status = new String(responseBody);
//
//            // 记录日志
//            EmailLog emailLog = new EmailLog();
//            emailLog.setReceiveEmail(tos);
//            emailLog.setReceiveUser(senderName);
//            emailLog.setTitle(titleCode);
//            emailLog.setMessage(contentCode);
//            emailLog.setSendTime(new Date());
//            if (StringUtils.isEmpty(status)){
//                emailLog.setErrorMessage(status);
//            }
//            ThreadUtil.THREAD_POOL_EXECUTOR.execute(()->{
//                emailUtil.emaillogService.savelog(emailLog);
//            });
        } catch (Exception e) {

        } finally {
            if (method != null) {
                method.releaseConnection();
            }
            client.getHttpConnectionManager().closeIdleConnections(0);
        }

        return status;
    }

    public static String transmitMsgByType(String tos, String title, String content) {

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"appId\" :\"interaction\",\n           \"appSecret\" :\"NfuNMqncAnjeernl\",\n           \"scopes\":[\"link_group\"]}");
        Request request = new Request.Builder()
                .url("https://i.haier.net/gateway/oauth2/getSysAccessToken")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "*/*")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Host", "i.haier.net")
                .addHeader("Connection", "keep-alive")
                .addHeader("cache-control", "no-cache")
                .build();
        try {
            JsonParser parse = new JsonParser();  //创建json解析器
            Response response = client.newCall(request).execute();
            JsonObject json = (JsonObject) parse.parse(response.body().string());  //创建jsonObject对象
            JsonObject result = json.get("data").getAsJsonObject();
            return result.get("accessToken").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
