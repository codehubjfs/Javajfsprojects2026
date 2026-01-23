package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.InputStream;

public class DBConnection {

    private static Connection connection = null;

    private DBConnection() {
        // private constructor to prevent object creation
    }

    public static Connection getConnection() {

        if (connection == null) {
            try {
                Properties props = new Properties();

                InputStream input = DBConnection.class
                        .getClassLoader()
                        .getResourceAsStream("application.properties");

                props.load(input);

                String url = props.getProperty("db.url");
                String username = props.getProperty("db.username");
                String password = props.getProperty("db.password");
                String driver = props.getProperty("db.driver");

                Class.forName(driver);

                connection = DriverManager.getConnection(url, username, password);

                System.out.println("Database connection established successfully.");

            } catch (Exception e) {
                System.out.println("Database connection failed.");
                e.printStackTrace();
            }
        }

        return connection;
    }
}
