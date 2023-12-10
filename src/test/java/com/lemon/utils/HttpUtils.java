package com.lemon.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

public class HttpUtils {
	
	private static Logger log = Logger.getLogger(HttpUtils.class);
	
	/**
	 * 静态在工具类中的作用:方便调用, 直接用类名点就能调用
	 * 静态： 共享
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
//		get("http://api.lemonban.com/futureloan/member/72312/info");
//		String json = "{\"mobile_phone\": \"13800123599\", \"pwd\": \"12345578\"}";
//		String url = "http://api.lemonban.com/futureloan/member/login";
//		jsonPost(url, json);
//		String form = "mobilephone=13212312333&pwd=123456";
//		String url2 = "http://test.lemonban.com/futureloan/mvc/api/member/login";
//		formPost(url2, form);
	}
	
	/**
	 * 接口调用方法
	 * @param url 接口请求地址
	 * @param method 接口请求方法
	 * @param params 接口请求参数
	 * @param contentType 接口类型
	 */
	public static String call(String url, String method, String params,String contentType, Map<String, String> headers) {
		String body=null;
		try {
			if("post".equalsIgnoreCase(method)) {
				if("form".equalsIgnoreCase(contentType))
				{
//					json-->key=value
					body = HttpUtils.formPost(url, json2KeyValue(params), headers);
				} else if("json".equalsIgnoreCase(contentType)) {
					body = HttpUtils.jsonPost(url, params,headers);
				}
			   
			} else if(method.equalsIgnoreCase("get")) {
				body = HttpUtils.get(url,headers);
			} else if(method.equalsIgnoreCase("patch")) {
				body = HttpUtils.patch(url, params,headers);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e);
			e.printStackTrace();
		}
		return body;
	}
	
	/**
	 * JSON字符串转成key=value字符串
	 * @param params  json字符串
	 * @return
	 */
	private static String json2KeyValue(String params) {
//		json-->key=value
				Map<String, String> map = JSON.parseObject(params, Map.class);
				Set<String> keySet = map.keySet();
				String result="";
				for(String key: keySet) {
					String value = map.get(key);
					result += key + "=" + value + "&";
				}
				params = result.substring(0,result.length()-1);
		return params;		
	}
	
	/**
	 * 给请求添加对应的请求头
	 * @param request 请求对象 (post、get、 patch)
	 * @param headers 请求头map
	 */
	public static void setHeaders(HttpRequest request,Map<String, String> headers) {
		//获取所有请求头里面key
		Set<String> keySet = headers.keySet();
		//遍历所有键
		for(String key: keySet) {
			//把对应键和值设置到request的header中
			request.setHeader(key, headers.get(key));
		}
		
	}
	
	/**
	 * 发送一个get请求
	 * 如果要修改请求头，请修改get方法
	 * @param url  接口的请求地址 + 请求参数
	 * @throws Exception
	 */
	public static String get(String url, Map<String, String> headers) throws Exception {
		HttpGet get = new HttpGet(url);
		//4.body header 如果有参数和header进行添加
//		get.setHeader("X-Lemonban-Media-Type", "lemonban.v1");
		setHeaders(get, headers);
		//5.send点击发送 创建HttpClient 客户端
		//XXXs(HttpClients)是HttpClient的工具类
		//XXXUtils是XXX的工具类
		HttpClient client = HttpClients.createDefault();
		//设置代理
//		HttpHost proxy = new HttpHost("127.0.0.1", 8889);
//		HttpResponse response = client.execute(proxy, get);
		//有了客户端就能发送请求，同时获取响应
		HttpResponse response = client.execute(get);
		//response body格式化响应体
		return printResponseAndReturnBody(response);
	}
	
	/**
	 * 发送一个post请求 json格式请求参数
	 * @param url   请求地址
	 * @param jsonParams json格式的请求参数
	 * @throws Exception
	 */
	public static String jsonPost(String url, String jsonParams, Map<String,String> headers) throws Exception{
		HttpPost post = new HttpPost(url);
//		post.setHeader("X-Lemonban-Media-Type", "lemonban.v1");
//		post.setHeader("Content-Type", "application/json");
		setHeaders(post, headers);
		post.setEntity(new StringEntity(jsonParams, "utf-8"));
		
		HttpClient client = HttpClients.createDefault();
		HttpResponse response = client.execute(post);
		return printResponseAndReturnBody(response);
	}
	
	/**
	 * 发送一个post请求 form请求参数
	 * @param url   请求地址
	 * @param jsonParams form格式的请求参数
	 * @throws Exception
	 */
	public static String formPost(String url, String formParams, Map<String,String> headers) throws Exception{
		HttpPost post = new HttpPost(url);
//		post.setHeader("X-Lemonban-Media-Type", "lemonban.v1");
//		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		setHeaders(post, headers);
		post.setEntity(new StringEntity(formParams, "utf-8"));
		
		HttpClient client = HttpClients.createDefault();
		HttpResponse response = client.execute(post);
		return printResponseAndReturnBody(response);
	}
	
	/**
	 * 发送一个patch请求
	 * @param url
	 * @param jsonParams
	 * @throws Exception
	 */
	public static String patch(String url, String jsonParams, Map<String, String> headers) throws Exception{
		HttpPatch patch = new HttpPatch(url);
//		patch.setHeader("X-Lemonban-Media-Type", "lemonban.v1");
//		patch.setHeader("Content-Type", "application/json");
		setHeaders(patch, headers);
		patch.setEntity(new StringEntity(jsonParams, "utf-8"));
		
		HttpClient client = HttpClients.createDefault();
		HttpResponse response = client.execute(patch);
		return printResponseAndReturnBody(response);
	}
	
	/**
	 * 打印响应内容,并返回响应体
	 * @param response 接口响应对象
	 * @return    接口响应体
	 * @throws Exception
	 */
	public static String printResponseAndReturnBody(HttpResponse response) throws Exception {
		//6.response body 格式响应体
		Header[] allHeaders = response.getAllHeaders();
		log.info("响应头：" + Arrays.toString(allHeaders) );
		HttpEntity entity = response.getEntity();
		String body = EntityUtils.toString(entity);
		log.info("响应体：" + body);
		int statusCode = response.getStatusLine().getStatusCode();
		log.info("状态码：" + statusCode);
		return body;
	}
	

}
