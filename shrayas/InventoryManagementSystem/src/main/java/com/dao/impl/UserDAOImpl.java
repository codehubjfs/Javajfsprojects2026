package com.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


import com.dao.UserDAO;
import com.enums.Gender;
import com.model.User;
import com.util.DatabaseConnectionPool;
import com.util.DateUtil;
import com.util.PasswordUtil;

public class UserDAOImpl implements UserDAO{

	    private static final String INSERT_USER = """
	        INSERT INTO user (first_name, last_name, email, password, phone, gender, role_id)
	        VALUES (?, ?, ?, ?, ?, ?, ?)
	    """;

	    private static final String FIND_BY_ID = """
	        SELECT * FROM user WHERE user_id = ?
	    """;

	    private static final String FIND_BY_EMAIL = """
	        SELECT * FROM user WHERE email = ?
	    """;

	    private static final String FIND_ALL = """
	        SELECT * FROM user
	    """;

	    private static final String UPDATE_USER = """
	        UPDATE user
	        SET first_name = ?, last_name = ?, phone = ?, gender = ?
	        WHERE user_id = ?
	    """;

	    private static final String INACTIVATE_USER = """
	        UPDATE user
	        SET status = 'INACTIVE'
	        WHERE user_id = ?	    
	    """;
	    
	public int createUser(User user) throws Exception{

		try(Connection connection = DatabaseConnectionPool.getConnection();
				PreparedStatement ps = connection.prepareStatement(INSERT_USER)){
			
			ps.setString(1, user.getFirstName());
			ps.setString(2, user.getLastName());
			ps.setString(3, user.getEmail());
			ps.setString(4, PasswordUtil.hashPassword(user.getPassword()));
			ps.setString(5, user.getPhone());
			ps.setString(6, user.getGender().name());
			ps.setInt(7, user.getRoleId());   

			
			return  ps.executeUpdate();
			
		}
	
	}
	
	public User findUserById(int userId) throws Exception{
		
		try(Connection con = DatabaseConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(FIND_BY_ID)){
			
			ps.setInt(1, userId);
			
			try(ResultSet rs = ps.executeQuery()){
				if (rs.next()) {
					return mapToUser(rs);
				}
			}
		}
		
		return null;
	}
	
	public User findUserByEmail(String email) throws Exception{
		
		try(Connection con = DatabaseConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(FIND_BY_EMAIL)){
			
			ps.setString(1, email);
			
			try(ResultSet rs = ps.executeQuery()){
				if(rs.next()) {
					return mapToUser(rs);
				}
			}
		}
		return null;
	}
	
	public List<User> findAll() throws Exception{
		
		List<User> users = new ArrayList<>();
		
		try(Connection con = DatabaseConnectionPool.getConnection();
				PreparedStatement st = con.prepareStatement(FIND_ALL);
				ResultSet rs = st.executeQuery()){
			
			while (rs.next()) {
				users.add(mapToUser(rs));
			}
		}
		return users;
	}
	
	public boolean update(User user) throws Exception{
		
		try(Connection con = DatabaseConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(UPDATE_USER)){
			
			ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getGender().name());
            ps.setInt(5, user.getUserId());
            
            return ps.executeUpdate() > 0;
		}

	}
	
	public boolean delete(int userId) throws Exception{
		
		try(Connection con = DatabaseConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(INACTIVATE_USER)){
			
			ps.setInt(1, userId);
			
			return ps.executeUpdate() > 0;
		}
	}
	
	
	private User mapToUser(ResultSet rs) throws SQLException {

        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setGender(Gender.valueOf(rs.getString("gender")));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setPhone(rs.getString("phone"));
        user.setRegisteredAt(DateUtil.toLocalDateTime(rs.getTimestamp("registered_at")));

        return user;
    }
}
