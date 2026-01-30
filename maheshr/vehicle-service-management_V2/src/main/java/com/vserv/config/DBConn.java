package com.vserv.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DB Config
 * 
 * @author Mahesh R
 */
public class DBConn {
    
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;
	static {
		try {
			Properties props = new Properties();
        
            InputStream input = DBConn.class
                    .getClassLoader()
                    .getResourceAsStream("application.properties-local");
            
            if (input == null) {
                throw new RuntimeException("application.properties-local not found in classpath");
            }
            
            props.load(input);
            
            URL = props.getProperty("db.url");
            USER = props.getProperty("db.user");
            PASSWORD = props.getProperty("db.password");
            
            if (URL == null || USER == null || PASSWORD == null) {
                throw new RuntimeException("Database configuration incomplete in properties file");
            }            
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Database configuration error: " + e.getMessage());
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    private DBConn() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}