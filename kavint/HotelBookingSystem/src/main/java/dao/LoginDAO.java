package dao;

import util.DBConnection;
import java.sql.*;

/**
 * This class will handle login data with database.
 * @author KavinT
 * @since 1.0
 */
public class LoginDAO {

	/**
	 * This method will register user to login database.
	 * @param userId
	 * @param password
	 * @param userType
	 * @throws Exception
	 */
    public void registerLogin(int userId, String password, String userType) throws Exception {
        String sql = "INSERT INTO login (user_id, password, user_type, created_at) VALUES (?, ?, ?, NOW())";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, password);
            ps.setString(3, userType);
            ps.executeUpdate();
        }
    }

    /**
     * This will return all login detail.
     * @param userId
     * @return
     * @throws Exception
     */
    public ResultSet getLoginByUserId(int userId) throws Exception {
        String sql = "SELECT * FROM login WHERE user_id=?";

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, userId);
        return ps.executeQuery();
    }

    /**
     * This will valid user login.
     * @param userId
     * @param password
     * @param userType
     * @return
     * @throws Exception
     */
    public boolean validateUser(int userId, String password, String userType) throws Exception {
        String sql = "SELECT 1 FROM login WHERE user_id=? AND password=? AND user_type=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, password);
            ps.setString(3, userType);

            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }
}
