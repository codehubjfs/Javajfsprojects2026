package DAO;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBUtil {
	public static Connection getConnection() throws Exception{
		Properties p = new Properties();
		try(InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("application.properties")){
			p.load(in);
		}
		
		return DriverManager.getConnection(
			p.getProperty("db.url"),
			p.getProperty("db.username"),
			p.getProperty("db.password"));
	}

}