package com.zj.customField.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.core.io.support.PropertiesLoaderUtils;

public class ConnectionUtil {

	private static final String filePath = "application.properties";
	
	public static Properties prop = null;
	
	private static void loadProperties(){
		try {
			prop = PropertiesLoaderUtils.loadAllProperties(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static {
		loadProperties();
		try {
			Class.forName(prop.getProperty("spring.datasource.driver-class-name"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Connection openConnection() {
		Connection con = null;
		try {
			con = DriverManager.getConnection(prop.getProperty("spring.datasource.url"),
					prop.getProperty("spring.datasource.username"), prop.getProperty("spring.datasource.password"));
			return con;
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
