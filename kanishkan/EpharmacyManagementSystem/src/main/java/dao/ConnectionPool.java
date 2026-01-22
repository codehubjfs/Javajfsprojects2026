package dao;

import java.util.Properties;
import java.sql.DriverManager;
import java.sql.Connection;
import java.io.InputStream;

public class ConnectionPool {
	
	public static Connection getConnection() throws Exception{
		Properties properties = new Properties();
		try(InputStream in = ConnectionPool.class.getClassLoader().getResourceAsStream("application.properties")){
			properties.load(in);
		}
		
		return DriverManager.getConnection(properties.getProperty("db.url")
				, properties.getProperty("db.username")
				, properties.getProperty("db.password"));
	}

}
