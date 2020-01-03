
package com.haier.hailian.contract.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;
@Slf4j
public class HttpContextUtils {

	public static HttpServletRequest getHttpServletRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	public static String getDomain(){
		HttpServletRequest request = getHttpServletRequest();
		StringBuffer url = request.getRequestURL();
		return url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
	}

	public static String getOrigin(){
		HttpServletRequest request = getHttpServletRequest();
		return request.getHeader("Origin");
	}

	/**
	 * post请求以及参数是json
	 *
	 * @param url
	 * @param jsonParams
	 * @return
	 */
	public static JSONObject doPostForJson(String url, String jsonParams) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		JSONObject jsonObject = null;
		HttpPost httpPost = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom().
				setConnectTimeout(180 * 1000).setConnectionRequestTimeout(180 * 1000)
				.setSocketTimeout(180 * 1000).setRedirectsEnabled(true).build();
		httpPost.setConfig(requestConfig);
		httpPost.setHeader("Content-Type", "application/json");
		//httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		try {
			httpPost.setEntity(new StringEntity(jsonParams, ContentType.create("application/json", "utf-8")));
			log.info("request parameters={}", EntityUtils.toString(httpPost.getEntity()));
			HttpResponse response = httpClient.execute(httpPost);
			if (response != null && response.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(response.getEntity());
				log.info("post utils returns ={}", result);
				jsonObject = (JSONObject)(new JSONParser().parse(result));
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
	public static JSONObject doPostHeaderForJson(String url, String jsonParams) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		JSONObject jsonObject = null;
		HttpPost httpPost = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom().
				setConnectTimeout(180 * 1000).setConnectionRequestTimeout(180 * 1000)
				.setSocketTimeout(180 * 1000).setRedirectsEnabled(true).build();
		httpPost.setConfig(requestConfig);
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("api_gateway_auth_app_id","8bbe7c30-2f11-48e6-9450-2a88a3c29eba");//应用id
		httpPost.setHeader("api_gateway_auth_app_password","Haier,789");//应用秘钥
		try {
			httpPost.setEntity(new StringEntity(jsonParams, ContentType.create("application/json", "utf-8")));
			log.info("request parameters={}", EntityUtils.toString(httpPost.getEntity()));
			HttpResponse response = httpClient.execute(httpPost);
			if (response != null && response.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(response.getEntity());
				log.info("post utils returns ={}", result);
				jsonObject = (JSONObject)(new JSONParser().parse(result));
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
