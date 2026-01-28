package DAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	
	
	//new customer registration
	public static boolean emailExists(String email) throws DBAccessException {
	    String sql = "select user_id from user where email = ?";
	    try (Connection con = DBUtil.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, email);
	        ResultSet rs = ps.executeQuery();
	        return rs.next();

	    } catch (SQLException | IOException e) {
	        throw new DBAccessException("Unable to check email availability");
	    }
	}


	public static boolean registerCustomerWithAddress(
	        String name, String email, String password,
	        String street, String city, String state, String pincode, String address_type
	) throws DBAccessException {

	    String userSql =
	            "insert into user (name, email, password, role, status) values (?, ?, ?, 'customer', 'active')";

	    String addressSql =
	            "insert into address (user_id, street, city, state, zipcode, address_type) values (?, ?, ?, ?, ?, ?)";

	    try (Connection con = DBUtil.getConnection()) {

	        con.setAutoCommit(false);

	        // 1️⃣ Insert into user table
	        try (PreparedStatement psUser =
	                     con.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {

	            psUser.setString(1, name);
	            psUser.setString(2, email);
	            psUser.setString(3, password);
	            psUser.executeUpdate();

	            ResultSet rs = psUser.getGeneratedKeys();
	            if (!rs.next()) {
	                con.rollback();
	                throw new DBAccessException("User registration failed.");
	            }

	            int userId = rs.getInt(1);

	            // 2️⃣ Insert address
	            try (PreparedStatement psAddress =
	                         con.prepareStatement(addressSql)) {

	                psAddress.setInt(1, userId);
	                psAddress.setString(2, street);
	                psAddress.setString(3, city);
	                psAddress.setString(4, state);
	                psAddress.setString(5, pincode);
	                psAddress.setString(6, address_type);
	                psAddress.executeUpdate();
	            }
	        }

	        con.commit();
	        return true;

	    } catch (SQLException | IOException e) {
	        throw new DBAccessException("Registration failed. Please try again.");
	    }
	}




}
