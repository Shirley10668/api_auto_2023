package com.lemon.httpclient;

import java.io.IOException;
import java.util.Arrays;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class GetDemo {
	
	public static void main(String[] args) throws IOException {
		
		//使用httpClient 发送一个get请求
		/***
		 * 1.new request 创建请求
		 * 2.add method 请求方式
		 * 3.url 填写url
		 * 4.body header 如果有参数和header
		 * 5.send 点击发送
		 * 6. response body 格式化响应体
		 */
		HttpGet get = new HttpGet("http://api.lemonban.com/futureloan/member/72312/info");
		//4.body header 如果有参数和header进行添加
		get.setHeader("X-Lemonban-Media-Type", "lemonban.v1");
		//5.send点击发送 创建HttpClient 客户端
		//XXXs(HttpClients)是HttpClient的工具类
		//XXXUtils是XXX的工具类
		HttpClient client = HttpClients.createDefault();
		//有了客户端就能发送请求，同时获取响应
		HttpResponse response = client.execute(get);
		//response = body + header + code...
		//6.response body 格式响应体
		Header[] allHeaders = response.getAllHeaders();
		System.out.println("响应头：" + Arrays.toString(allHeaders) );
		HttpEntity entity = response.getEntity();
		String body = EntityUtils.toString(entity);
		System.out.println("响应体：" + body);
		int statusCode = response.getStatusLine().getStatusCode();
		System.out.println("状态码：" + statusCode);
		
	}

}
