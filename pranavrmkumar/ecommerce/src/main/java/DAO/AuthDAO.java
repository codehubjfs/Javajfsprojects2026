package DAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Exceptions.DataAccessException;
import Model.Admin;
import util.DBUtil;

public class AuthDAO {
	
	//admin login
	public static Admin adminLogin(String email,String password) throws DataAccessException{
		String sql = "select user_id,name,email,role from user where email = ? and password = ? and role = 'admin'";
		try(Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
			ps.setString(1, email);
			ps.setString(2, password);
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				return new Admin(rs.getString("name"),rs.getString("email"),rs.getString("role"));
			}
			return null;
		}catch(SQLException | IOException e) {
			throw new DataAccessException("Database error during login");
		}
	}
}
