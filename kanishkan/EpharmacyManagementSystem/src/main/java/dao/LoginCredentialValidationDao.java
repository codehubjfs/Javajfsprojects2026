package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;

import model.User;

public class LoginCredentialValidationDao {
	
	public static User validateLogin(String phone, String password) throws Exception{
		String query = "select * from users where phone = ? and password = ?;";
		int userId = 0;
		int roleId = 0;
		User user = new User();
		try(Connection conn = ConnectionPool.getConnection();
				PreparedStatement ps = conn.prepareStatement(query)){
			
			ps.setString(1, phone);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				user.setUserId(rs.getInt("user_id"));
				user.setRoleId(rs.getInt("role_id"));
			}
			
		}
		return user;
	}

}
