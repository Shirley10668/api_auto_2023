package com.lemon.pojo;

import javax.validation.constraints.NotNull;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class API {
	//接口编号   接口名称  接口提交方式 接口地址  参数类型
	@Excel(name="接口编号") //@Excel : excel列和实体类的成员变量映射关系
	@NotNull
	private String id;  //接口编号
	
	@Excel(name="接口名称")
	private String name;  //接口名称
	
	@Excel(name="接口提交方式")
	private String method; //接口请求方法
	
	@Excel(name="接口地址")
//	@URL(protocol="http",host="api.lemonban.com")
	private String url;   //接口请求地址
	
	@Excel(name="参数类型")
	private String contentType; //接口请求参数类型
	
	public API() {
		super();
		// TODO Auto-generated constructor stub
	}

	public API(String id, String name, String method, String url, String contentType) {
		super();
		this.id = id;
		this.name = name;
		this.method = method;
		this.url = url;
		this.contentType = contentType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
	public String toString() {
		return "API [id=" + id + ", name=" + name + ", method=" + method + ", url=" + url + ", contentType="
				+ contentType + "]";
	}
	
}
