package com.haier.hailian.contract.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.util.StringUtils;

import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

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

//    public static String transmitMsgByType(String tos, String title, String content, String type) {
//
//        String status = "";
//        PostMethod method = null;
//        HttpClient client = new HttpClient();
//        int statusCode = 0;
//        client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
//        // 设置连接超时时间为30秒（连接初始化时间）
//        client.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
//        try {
//            String contentCode = content;
//            String titleCode = title;
//            //对于特殊字符处理
//            content = URLEncoder.encode(content, "utf-8");
//            title = URLEncoder.encode(title, "utf-8");
//            String senderName = "";
//            String attachmentName = "";
//            senderName = URLEncoder.encode(senderName, "utf-8");
//            attachmentName = URLEncoder.encode(attachmentName, "utf-8");
//            method = new PostMethod(
//                    "http://his.haier.net:8989/services/smsemail/sendHisEmail");
//            method.setParameter("tos", tos);
//            method.setParameter("title", title);
//            method.setParameter("content", content);
//            method.setParameter("system", "OTCsys");
//            method.setParameter("from", "iof@haier.com");
//            method.setParameter("senderName", senderName);
//            method.setParameter("Cc", "");
//            method.setParameter("Bcc", "");
//            method.setParameter("attachmentName", "");
//            method.setParameter("attachmentBytes", "");
//            method.setParameter("eventHisId", "");
//            method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=utf-8");
//            statusCode = client.executeMethod(method);
//
//            // 读取内容
//            byte[] responseBody = method.getResponseBody();
//            // 处理内容
//            status = new String(responseBody);
//
////            // 记录日志
////            EmailLog emailLog = new EmailLog();
////            emailLog.setReceiveEmail(tos);
////            emailLog.setReceiveUser(senderName);
////            emailLog.setTitle(titleCode);
////            emailLog.setMessage(contentCode);
////            emailLog.setSendTime(new Date());
////            emailLog.setSendType(type);
////            if (StringUtils.isEmpty(status)){
////                emailLog.setErrorMessage(status);
////            }
////            ThreadUtil.THREAD_POOL_EXECUTOR.execute(()->{
////                emailUtil.emaillogService.savelog(emailLog);
////            });
//        } catch (Exception e) {
//
//        } finally {
//            if (method != null) {
//                method.releaseConnection();
//            }
//            client.getHttpConnectionManager().closeIdleConnections(0);
//        }
//
//        return status;
//    }

}
