package dao;

import util.DBConnection;
import java.sql.*;

/**
 * This class will handle the user data with database.
 * @author KavinT
 * @since 1.0
 */
public class UserDAO {

    /**
     * This method will create new user and return generated user_id
     * @param name
     * @param email
     * @param phone
     * @param gender
     * @param locationId
     * @return
     * @throws Exception
     */
    public int createUser(String name, String email, String phone, String gender, int locationId) throws Exception {

        String sql = "INSERT INTO user (name, email, phone, gender, location_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, gender);
            ps.setInt(5, locationId);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new Exception("User ID not generated!");
        }
    }

    /**
     * This method will return user detail by user id.
     * @param userId
     * @return
     * @throws Exception
     */
    public ResultSet getUserById(int userId) throws Exception {
        String sql = "SELECT * FROM user WHERE user_id = ?";

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, userId);

        return ps.executeQuery();
    }
}
