package com.lemon.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.testng.log4testng.Logger;

import com.lemon.constants.Constants;

public class JDBCUtils {
	
	public static Logger log = Logger.getLogger(JDBCUtils.class);
	
	public static Connection getConnection() {
		//定义数据库连接 
		//定义数据库连接对象
		Connection conn = null;
		try {
			//你导入的数据库驱动包， mysql。
			conn = DriverManager.getConnection(Constants.JDBC_URL, Constants.JDBC_USERNAME,Constants.JDBC_PASSWORD);
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return conn;
	}


	public static void close(Connection conn) {
		try {
				if(conn!=null) {
				   conn.close();
				}
			} catch (SQLException e) {
				log.error(e);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
