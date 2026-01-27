package DAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Exceptions.DBAccessException;
import Exceptions.InvalidCredentialsException;
import Model.Admin;
import Model.Customer;
import util.DBUtil;

public class AuthDAO {
	
	//admin login
	public static Admin adminLogin(String email,String password) throws DBAccessException{
		String sql = "select name,email,role from user where email = ? and password = ? and role = 'admin'";
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
			throw new DBAccessException("Database error during login");
		}
	}
	
	
	
	public static Customer customerLogin(String email, String password)
            throws DBAccessException, InvalidCredentialsException {
        String sql = "select name, email, status from user where email = ? and password = ? and role = 'customer'";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new InvalidCredentialsException("Invalid email or password.");
            }
            if ("inactive".equalsIgnoreCase(rs.getString("status"))) {
                throw new InvalidCredentialsException("Account is blocked. Contact support.");
            }
            return new Customer(
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("status")
            );

        } catch (SQLException | IOException e) {
            throw new DBAccessException("Unable to login. Please try again later.");
        }
	}
}
