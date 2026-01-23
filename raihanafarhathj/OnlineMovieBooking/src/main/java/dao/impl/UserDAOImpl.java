package dao.impl;

import dao.UserDAO;
import model.User;
import enums.Gender;
import enums.UserStatus;
import exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import config.DBConnection;

public class UserDAOImpl implements UserDAO {

    @Override
    public int addUser(User user) throws DataAccessException {
        String sql = """
            INSERT INTO user
            (name, email, password, dob, phone_number, gender, status, role_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setDate(4, Date.valueOf(user.getDob()));
            ps.setString(5, user.getPhoneNumber());
            ps.setString(6, user.getGender().name());
            ps.setString(7, user.getStatus().name());
            ps.setInt(8, user.getRoleId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            throw new DataAccessException("Unable to fetch generated user ID");

        } catch (SQLException e) {
            throw new DataAccessException("Error while adding user", e);
        }
    }

    @Override
    public User getUserById(int userId) throws DataAccessException {
        String sql = "SELECT * FROM user WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching user by ID", e);
        }
    }

    @Override
    public User getUserByEmail(String email) throws DataAccessException {
        String sql = "SELECT * FROM user WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching user by email", e);
        }
    }

    @Override
    public List<User> getUsersByRole(int roleId) throws DataAccessException {
        String sql = "SELECT * FROM user WHERE role_id = ?";
        List<User> users = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roleId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(mapUser(rs));
                }
            }
            return users;

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching users by role", e);
        }
    }

    @Override
    public boolean updateUserStatus(int userId, String status) throws DataAccessException {
        String sql = "UPDATE user SET status = ? WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, userId);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new DataAccessException("Error updating user status", e);
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();

        user.setUserId(rs.getInt("user_id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setDob(rs.getDate("dob").toLocalDate());
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setGender(Gender.valueOf(rs.getString("gender")));
        user.setStatus(UserStatus.valueOf(rs.getString("status")));
        user.setRoleId(rs.getInt("role_id"));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        return user;
    }
}
