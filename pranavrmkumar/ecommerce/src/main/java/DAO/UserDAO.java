package DAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Exceptions.DBAccessException;
import Model.Address;
import util.DBUtil;

public class UserDAO {
	public static int getUserIdByEmail(String email) throws DBAccessException {

	    String sql = "select user_id from user where email=? and status='active'";

	    try (Connection con = DBUtil.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, email);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            return rs.getInt("user_id");
	        }

	        throw new DBAccessException("User not found or inactive.");

	    } catch (SQLException | IOException e) {
	        throw new DBAccessException("Unable to fetch user.");
	    }
	}
	
	
	
	public static int getAddressIDByEmail(String email) throws DBAccessException {

        String sql = "select a.address_id from address a join user u on a.user_id = u.user_id where u.email = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("address_id");
            } else {
                return 0;
            }

        } catch (SQLException | IOException e) {
            throw new DBAccessException("Unable to fetch delivery address.");
        }
    }



	public static void updateUserName(int userIdByEmail, String newName) throws DBAccessException {
		// TODO Auto-generated method stub
		String sql = "update user set name = ? where user_id = ?";
		try(Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
			ps.setString(1, newName);
			ps.setInt(2, userIdByEmail);
			int rows = ps.executeUpdate();
			if(rows == 0) {
				throw new DBAccessException("User not found");
			}
		}catch(SQLException | IOException e) {
			throw new DBAccessException("Unable to update user name");
		}
	}



	public static void updateUserEmail(int userIdByEmail, String newEmail) throws DBAccessException {
		// TODO Auto-generated method stub
		String sql = "update user set email = ? where user_id = ?";
		try(Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
			ps.setString(1, newEmail);
			ps.setInt(2, userIdByEmail);
			int rows = ps.executeUpdate();
			if(rows == 0) {
				throw new DBAccessException("User not found");
			}
		}catch(SQLException | IOException e) {
			throw new DBAccessException("Unable to update user email");
		}
	}



	public static void updateUserPassword(int userIdByEmail, String newPassword) throws DBAccessException{
		// TODO Auto-generated method stub
		String sql = "update user set password = ? where user_id = ?";
		try(Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
			ps.setString(1, newPassword);
			ps.setInt(2, userIdByEmail);
			int rows = ps.executeUpdate();
			if(rows == 0) {
				throw new DBAccessException("User not found");
			}
		}catch(SQLException | IOException e) {
			throw new DBAccessException("Unable to update user password");
		}
	}



	public static List<Address> getAddressesByUserId(int userId) throws DBAccessException{
		// TODO Auto-generated method stub
		List<Address> addresses = new ArrayList<>();
		String sql = "select * from address where user_id = ?";
		try(Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				addresses.add(new Address(rs.getInt("address_id"),rs.getString("street"),rs.getString("city"),rs.getString("state"),rs.getString("zipcode"),rs.getString("address_type")));
			}
		}catch(SQLException | IOException e) {
			throw new DBAccessException("Unable to fetch addresses");
		}
		return addresses;
	}



	public static void addAddress(int userId, String street, String city, String state, String zipcode, String type) throws DBAccessException {
		// TODO Auto-generated method stub
		String sql = "insert into address(user_id,street,city,state,zipcode,address_type) values (?,?,?,?,?,?)";
		try(Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
			ps.setInt(1, userId); 
			ps.setString(2, street); 
			ps.setString(3, city); 
			ps.setString(4, state); 
			ps.setString(5, zipcode); 
			ps.setString(6, type); 
			ps.executeUpdate();
		}catch(SQLException | IOException e) {
			throw new DBAccessException("Unable to add address.");
		}
	}



	public static void updateAddress(int addressId, String street, String city, String state, String zipcode,
			String type) throws DBAccessException {
		// TODO Auto-generated method stub
		String sql = "update address set street = ?,city = ?,state = ?,zipcode = ?,address_type = ? where address_id = ?";
		try(Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, street); 
			ps.setString(2, city); 
			ps.setString(3, state); 
			ps.setString(4, zipcode); 
			ps.setString(5, type); 
			ps.setInt(6, addressId); 
			int rows = ps.executeUpdate(); 
			if (rows == 0) { 
				throw new DBAccessException("Address not found."); 
			}
		}catch(SQLException | IOException e) {
			throw new DBAccessException("Unable to update address.");
		}
	}



	public static void deleteAddress(int addressId) throws DBAccessException {
		// TODO Auto-generated method stub
		String sql = "delete from address where address_id = ?"; 
		try (Connection con = DBUtil.getConnection(); 
				PreparedStatement ps = con.prepareStatement(sql)) { 
			ps.setInt(1, addressId); 
			int rows = ps.executeUpdate(); 
			if (rows == 0) { 
				throw new DBAccessException("Address not found."); 
			} 
		} catch (SQLException | IOException e) { 
			throw new DBAccessException("Unable to delete address."); 
		}
	}
	
	

}
