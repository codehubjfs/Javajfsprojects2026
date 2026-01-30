package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;

public class DBUtil {

    public static Connection getConnection() throws SQLException, IOException {
    	//Class.forName("com.mysql.cj.jdbc.Driver");
        Properties props = new Properties();
        try (InputStream is = DBUtil.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (is == null) {
                throw new RuntimeException("application.properties not found in classpath");
            }
            props.load(is);
        }
        return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.username"),
                props.getProperty("db.password")
        );
        
        
    }
}

