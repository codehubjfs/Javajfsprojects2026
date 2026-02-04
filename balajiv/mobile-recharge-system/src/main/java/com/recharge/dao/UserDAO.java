package com.recharge.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.recharge.config.DBConnection;
import com.recharge.model.Role;
import com.recharge.model.User;

public class UserDAO {
	
	// query used to find the user by email
	private static final String FIND_BY_EMAIL = 
			"select u.user_id, u.full_name, u.gender, u.email, u.password_hash, " +
			"u.status, u.created_at, r.role_name " +
			"from users u join role r on u.role_id = r.role_id " +
			"where u.email = ?";
	
	/**
	 * used to find the user by email
	 * @param email
	 * @return user object
	 */
	public User findByEmail(String email) {
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(FIND_BY_EMAIL);
			ps.setString(1, email);
			
			ResultSet rs = ps.executeQuery();
			
			if(!rs.next()) {
				return null;
			}
			
			return new User(
                    rs.getInt("user_id"),
                    rs.getString("full_name"),
                    rs.getString("gender"),
                    rs.getString("email"),
                    rs.getString("password_hash"),
                    Role.valueOf(rs.getString("role_name").toUpperCase()),
                    rs.getString("status"),
                    rs.getTimestamp("created_at").toLocalDateTime()
            );
			
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to fetch user by email", e);
		}
		
	}
	
	/**
	 * used to verify the existence of user by email
	 * @param email
	 * @return
	 */
	public boolean existsByEmail(String email) {

	    String sql = "SELECT COUNT(*) FROM users WHERE email = ?";

	    try {
	        Connection conn = DBConnection.getConnection();
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, email);

	        ResultSet rs = ps.executeQuery();
	        rs.next();
	        return rs.getInt(1) > 0;

	    } catch (Exception e) {
	        throw new RuntimeException("Failed to check email existence", e);
	    }
	}
	
	/**
	 * used to insert the values to the user table
	 * @param user
	 */
	public void save(User user) {

	    String sql =
	        """
	        INSERT INTO users
	        (full_name, gender, email, password_hash, role_id, status, created_at)
	        VALUES (?, ?, ?, ?, ?, 'ACTIVE', NOW())
	        """;

	    try {
	        Connection conn = DBConnection.getConnection();
	        PreparedStatement ps = conn.prepareStatement(sql);

	        ps.setString(1, user.getFullName());
	        ps.setString(2, user.getGender());
	        ps.setString(3, user.getEmail());
	        ps.setString(4, user.getPasswordHash());
	        ps.setInt(5, user.getRole().getId());

	        ps.executeUpdate();

	    } catch (Exception e) {
	        throw new RuntimeException("Failed to register user", e);
	    }
	}

}
