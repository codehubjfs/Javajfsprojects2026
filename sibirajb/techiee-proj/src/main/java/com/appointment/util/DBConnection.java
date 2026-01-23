package com.appointment.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Database configuration keys
    private static final String DB_URL = "db.url";
    private static final String DB_USERNAME = "db.username";
    private static final String DB_PASSWORD = "db.password";
    private static final String DB_DRIVER = "db.driver";
    
    private static Connection connection = null;

    // Private constructor to prevent instantiation
    private DBConnection() {}

    /**
     * Get database connection (Singleton pattern)
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Load configuration from properties file
                String url = ConfigReader.getProperty(DB_URL);
                String username = ConfigReader.getProperty(DB_USERNAME);
                String password = ConfigReader.getProperty(DB_PASSWORD);
                String driver = ConfigReader.getProperty(DB_DRIVER);
                
                if (url == null || username == null || password == null) {
                    throw new RuntimeException(" Database configuration not found in application.properties");
                }
                
                // Load MySQL JDBC Driver
                if (driver != null) {
                    Class.forName(driver);
                } else {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                }
                
                // Establish connection
                connection = DriverManager.getConnection(url, username, password);
//                System.out.println(" Database connected successfully!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println(" MySQL JDBC Driver not found!");
            e.printStackTrace();
            throw new RuntimeException("Database driver not found", e);
        } catch (SQLException e) {
            System.err.println(" Database connection failed!");
            e.printStackTrace();
            throw new RuntimeException("Database connection failed", e);
        }
        return connection;
    }

    /**
     * Close database connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println(" Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println(" Error closing database connection!");
            e.printStackTrace();
        }
    }

    /**
     * Test database connection
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            System.err.println(" Connection test failed: " + e.getMessage());
            return false;
        }
    }
}