package com.appointment.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Properties properties = new Properties();
    private static final String PROPERTIES_FILE = "application.properties";
    private static boolean isLoaded = false;
    
    public static String getProperty(String key) {
        if (!isLoaded) {
            loadProperties();
        }
        return properties.getProperty(key);
    }
    
    public static String getProperty(String key, String defaultValue) {
        if (!isLoaded) {
            loadProperties();
        }
        return properties.getProperty(key, defaultValue);
    }
    public static String getRequiredProperty(String key) {
        String value = getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("Required property '" + key + "' not found in " + PROPERTIES_FILE);
        }
        return value;
    }    
    private static synchronized void loadProperties() {
        if (isLoaded) return;
        
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE)) {     
            if (input == null) {
                throw new RuntimeException("Configuration file '" + PROPERTIES_FILE + "' not found in classpath");
            }
            
            properties.load(input);
            isLoaded = true;
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file: " + PROPERTIES_FILE, e);
        }
    }
}