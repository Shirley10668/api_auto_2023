package com.lemon.cases;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.lemon.constants.Constants;
import com.lemon.pojo.API;
import com.lemon.pojo.Case;
import com.lemon.utils.ExcelUtils;
import com.lemon.utils.HttpUtils;
import com.lemon.utils.SQLUtils;

import io.qameta.allure.Description;

public class RegisterCase extends BaseCase{
	
	
	/**
	 * 注册用例的测试方法
	 * @param url  接口请求地址
	 * @param method  接口请求方法
	 * @param params  接口请求参数
	 */
	@Test(dataProvider = "datas", description="注册测试")
	@Description("注册测试11111")
	public void test(API api, Case c) {
		//1、参数化替换
		c.setParams(paramsReplace(c.getParams()));
		c.setSql(paramsReplace(c.getSql()));
		//2. 数据库前置查询结果(数据断言必须在接口执行前后都查询)
		Object beforeSQLResult = SQLUtils.getSQLSingleResult(c.getSql());
		//3、调用接口
		Map<String,String> headers = new HashMap<String,String>();
        //设置默认请求头
		setDefaultHeaders(headers);
		String body = HttpUtils.call(api.getUrl(), api.getMethod(), c.getParams(), api.getContentType(),headers);
		//4.断言响应结果
		String responseAssert = responseAssert(c.getExpect(), body);
		//5.添加接口响应回写内容
		addWriteBackData(1,c.getId(),Constants.ACTUAL_RESPONSE_CELLNUM, body);
	    //6.添加数据库后置结果
		Object afterSQLResult = SQLUtils.getSQLSingleResult(c.getSql());
		//7.数据库断言
		boolean sqlAssertFlag = true;
		if(StringUtils.isNotBlank(c.getSql())) {
		  sqlAssertFlag = sqlAssert(beforeSQLResult, afterSQLResult);
		  System.out.println("数据库断言：" + sqlAssertFlag);
		  //sql断言回写
		  addWriteBackData(1, c.getId(), Constants.SQL_ASSERT_CELLNUM, sqlAssertFlag?"Pass":"Fail");
		}
		//8.添加断言回写内容
		addWriteBackData(1, c.getId(),Constants.RESPONSE_ASSERT_CELLNUM, responseAssert);
		//9. 添加日志
		//10.报表断言
		Assert.assertEquals(responseAssert, "断言成功");
		Assert.assertEquals(sqlAssertFlag, true);
		
	
	}
	
	/**
	 * 数据库断言
	 * @param beforeSQLResult 接口执行之前的数据结果
	 * @param afterSQLResult  接口执行之后的数据结果
	 * @return
	 */
	public Boolean sqlAssert(Object beforeSQLResult, Object afterSQLResult) {
		Long beforeValue = (Long)beforeSQLResult;
		Long afterValue = (Long)afterSQLResult;
		if(beforeValue==0 && afterValue==1) {
			//接口执行之前手机号码统计为0，执行之后手机号码统计为1，断言成功
			return true;
		}
		
		return false;
	}

	
	@DataProvider
	public Object[][] datas(){
		Object[][] datas = ExcelUtils.getAPIAndCaseByApiId("1");
		return datas;
	}
	

	
	@DataProvider
	public Object[][] datas2(){
		Object[][] datas = {
				{"http://api.lemonban.com/futureloan/member/login","POST", "{\"mobile_phone\": \"13213450023\", \"pwd\":\"12345677\"}"},
				{"http://api.lemonban.com/futureloan/member/login","POST", "{\"mobile_phone\": \"13213453720\", \"pwd\":\"12345677\"}"},
				{"http://api.lemonban.com/futureloan/member/login","POST", "{\"mobile_phone\": \"13213455469\", \"pwd\":\"12345677\"}"}
		};
		return datas;
	}
	

}
