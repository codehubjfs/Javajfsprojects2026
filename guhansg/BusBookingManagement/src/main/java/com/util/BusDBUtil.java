package com.util;

import java.io.InputStream;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class BusDBUtil {
	public static Connection getConnection() throws Exception{
    	Properties props = new Properties();
    	try(InputStream is = BusDBUtil.class.getClassLoader()
    			.getResourceAsStream("application.properties")){
    		props.load(is);
    	}
    	return DriverManager.getConnection(
    			props.getProperty("db.url"),
    			props.getProperty("db.username"),
    			props.getProperty("db.password")
    			);
    }

}
