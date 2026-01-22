package com.util;

import java.sql.Connection;
import java.sql.DriverManager;

import com.exception.ResourceNotFoundException;

public final class DatabaseConnectionPool {

	private DatabaseConnectionPool() {}	
	
	private static final String DB_URL = System.getenv("DB_URL");
	private static final String DB_USERNAME = System.getenv("DB_USERNAME");
	private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");

	public static Connection getConnection() throws Exception{
		if (DB_URL == null || DB_USERNAME == null || DB_PASSWORD == null) {
			throw new ResourceNotFoundException("Database environment variables are not configured properly");
		}
		
		return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
	}
}
