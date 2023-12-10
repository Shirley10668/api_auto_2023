package com.lemon.cases;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONPath;
import com.lemon.constants.Constants;
import com.lemon.pojo.API;
import com.lemon.pojo.Case;
import com.lemon.utils.EnvironmentUtils;
import com.lemon.utils.ExcelUtils;
import com.lemon.utils.HttpUtils;
import com.lemon.utils.SQLUtils;

public class RechargeCase extends BaseCase{
	
	@Test(dataProvider="datas")
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
		getToken2Header(headers);
//		String token = EnvironmentUtils.env.get("${token}");
//		if(StringUtils.isNoneBlank(token)) {
//		    headers.put("Authorization", "Bearer " + token);
//		}
//		System.out.println("token:" + token);
//		Set<String> keySet = headers.keySet();
//		for(String key: keySet) {
//			System.out.println("key:" + key + " ,value:" + headers.get(key));
//		}
		String body = HttpUtils.call(api.getUrl(), api.getMethod(), c.getParams(), api.getContentType(),headers);
		//4.断言响应结果
		String responseAssert = responseAssert(c.getExpect(), body);
		//5.添加接口响应回写内容
		addWriteBackData(1,c.getId(),Constants.ACTUAL_RESPONSE_CELLNUM, body);
		Object afterSQLResult = SQLUtils.getSQLSingleResult(c.getSql());
		//7.数据库断言
		boolean sqlAssertFlag = true;
		if(StringUtils.isNotBlank(c.getSql())) {
		  sqlAssertFlag = sqlAssert(beforeSQLResult, afterSQLResult, c);
		  System.out.println("数据库断言：" + sqlAssertFlag);
		  //回写数据库
		  addWriteBackData(1, c.getId(), Constants.SQL_ASSERT_CELLNUM, sqlAssertFlag?"Pass":"Fail");
		}
		//8.添加断言回写内容
		addWriteBackData(1, c.getId(), Constants.RESPONSE_ASSERT_CELLNUM, responseAssert);
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
	public Boolean sqlAssert(Object beforeSQLResult, Object afterSQLResult,Case c) {
		if(beforeSQLResult==null || afterSQLResult==null) {
			return false;
		}
		//从参数中获取amount的值
		String params = c.getParams();
		String amount = JSONPath.read(params, "$.amount").toString();
		//把amount转成BigDecimal
		BigDecimal amountValue = new BigDecimal(amount);
		//beforeSQLReuslt和afterSQLReuslt 转成 BigDecimal
		BigDecimal beforeValue = (BigDecimal)beforeSQLResult;
		BigDecimal afterValue = (BigDecimal)afterSQLResult;
		//afterValue - beforeValue = 实际的充值金额
	    BigDecimal subtractResult = afterValue.subtract(beforeValue);
	    //参数amount（期望值） 和 subtractResult（实际值） 进行比较，如果是0说明相等
		if(subtractResult.compareTo(amountValue)==0) {
			return true;
		}
		return false;
	}
	
	@DataProvider
	public Object[][] datas(){
		Object[][] datas = ExcelUtils.getAPIAndCaseByApiId("3");
		return datas;
	}

}
