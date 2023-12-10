package com.lemon.utils;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;
import org.testng.log4testng.Logger;

import com.lemon.pojo.Member;

public class SQLUtils {
	
	public static Logger log = Logger.getLogger(SQLUtils.class);
	
	public static void main(String[] args) throws Exception {
		//DBUtils
		QueryRunner qr = new QueryRunner();
		//conn数据库
		Connection conn = JDBCUtils.getConnection();
//		Object[] objects =
//				qr.query(conn, "select * from member a where a.id = 10;", new ArrayHandler());
//		for(Object object: objects) {
//			System.out.println(object);
//		}
		
//		Member member = qr.query(conn, "select * from member a where a.id = 10;", new BeanHandler<Member>(Member.class));
//        System.out.println(member);
        
		Object object = qr.query(conn,"select count(*) from member a where a.id = 10 ;",new ScalarHandler<Object>());
		System.out.println(object);
		JDBCUtils.close(conn);
	}
	
	/**
	 * 根据sql语句执行查询单个结果集
	 * @param sql   sql语句
	 * @return
	 */
	public static Object getSQLSingleResult(String sql) {
		//如果sql为空，则不执行sql查询
		if(StringUtils.isBlank(sql)) {
			return null;
		}
		//DBUtils操作sql语句核心类
		QueryRunner qr = new QueryRunner();
		//获取数据库连接
		Connection conn = JDBCUtils.getConnection();
		//定义返回值
		Object result = null;
		try {
			//执行sql语句
			result = qr.query(conn, sql, new ScalarHandler<Object>());
		} catch(SQLException e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			JDBCUtils.close(conn);
		}
		
		return result;
	}
	
	public static Member getOneRandMember() {
		String sql = "select * from member order by rand() limit 1;";
		//DBUtils操作sql语句核心类
		QueryRunner qr = new QueryRunner();
		Connection conn = JDBCUtils.getConnection();
		Member result = null;
		try {
			result = qr.query(conn,sql, new BeanHandler<Member>(Member.class));
			
		} catch(Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			JDBCUtils.close(conn);
		}
	    return result;			
	}

}
