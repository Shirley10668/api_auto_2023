package com.lemon.httpclient;

import java.io.IOException;
import java.util.Arrays;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class PostFormDemo {
	
	public static void main(String[] args) throws Exception {
		
		//使用httpClient 发送一个post请求 支持form参数
		/***
		 * 1.new request 创建请求
		 * 2.add method 请求方式
		 * 3.url 填写url
		 * 4.body header 如果有参数和header进行添加
		 * 5.send 点击发送
		 * 6. response body 格式化响应体
		 */
		HttpPost post = new HttpPost("http://test.lemonban.com/futureloan/mvc/api/member/login");
		post.setHeader("X-Lemonban-Media-Type", "lemonban.v1");
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		
		String json = "mobilephone=13212312333&pwd=123456";
		post.setEntity(new StringEntity(json, "utf-8"));
		
		HttpClient client = HttpClients.createDefault();
		HttpResponse response = client.execute(post);
		Header[] allHeaders = response.getAllHeaders();
		System.out.println("响应头：" + Arrays.toString(allHeaders));
		HttpEntity entity = response.getEntity();
		String body = EntityUtils.toString(entity);
		System.out.println("响应体：" + body);
		int statusCode = response.getStatusLine().getStatusCode();
		System.out.println("状态码：" + statusCode);
		
		
	}

}
