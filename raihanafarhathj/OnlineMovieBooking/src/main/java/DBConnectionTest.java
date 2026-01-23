import java.sql.Connection;

import config.DBConnection;

public class DBConnectionTest {

    public static void main(String[] args) {

        Connection con = DBConnection.getConnection();

        if (con != null) {
            System.out.println("Connection established successfully!");
        } else {
            System.out.println("Connection failed!");
        }
    }
}
