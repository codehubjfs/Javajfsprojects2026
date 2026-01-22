package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

import com.util.BusDBUtil;

public class UserDAO {
		
		/*String sql = "INSERT INTO User(user_name, user_email,user_mobile,user_password,user_role) VALUES (?,?,?,?,?)";
        try (Connection con = BusDBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userName);
            ps.setString(2, userEmail);
            ps.setString(3, mobile);
            ps.setString(4,password);
            ps.setString(5, role);
            ps.executeUpdate();
        }
	}*/
	public static String checkingAdminLogin(String email,String password) throws Exception{
		String admin = "";
		String sql = "select * from Users where user_email = ? and user_password = ? and role = 'ADMIN';";
		try(Connection con = BusDBUtil.getConnection();PreparedStatement ps = con.prepareStatement(sql)){
			ps.setString(1,email);
			ps.setString(2,password);
			
			try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                admin = rs.getString("user_name");
	            }
	        }
		}
	
		return admin;
	}

	public static int checkingOperatorLogin(String email,String password) throws Exception{
		String sql = "select * from Users where user_email = ? and user_password = ? and role = 'OPERATOR';";
		try(Connection con = BusDBUtil.getConnection();PreparedStatement ps = con.prepareStatement(sql)){
			ps.setString(1,email);
			ps.setString(2,password);
			
			try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt("user_id");
	            }
	        }
		}
		
		return -1;
	}
	
	public static void customerRegistration(String name,String mobile,String email,
			String password,LocalDate createDate) throws Exception{
		String sql = "insert into Users(user_name,user_mobie,user_email,"
				+ "user_password,created_date,role) values(?,?,?,?,?,'CUSTOMER')";
		try(Connection con = BusDBUtil.getConnection();PreparedStatement ps = con.prepareStatement(sql)){
			ps.setString(1, name);
			ps.setString(2, mobile);
			ps.setString(3,email);
			ps.setString(4, password);
			ps.setDate(5, java.sql.Date.valueOf(createDate));
			ps.executeUpdate();
		}
	}
	
	public static void addNewOperator(String name,String mobile,String email,String password,LocalDate createDate)throws Exception{
		String sql = "insert into Users(user_name,user_mobie,user_email,user_password,created_date,role) values(?,?,?,?,?,'OPERATOR')";
		try(Connection con = BusDBUtil.getConnection();PreparedStatement ps = con.prepareStatement(sql)){
			ps.setString(1, name);
			ps.setString(2, mobile);
			ps.setString(3,email);
			ps.setString(4, password);
			ps.setDate(5, java.sql.Date.valueOf(createDate));
			ps.executeUpdate();
		}
	}
	
	public void removeExistigOperator(String name,String mobile) throws Exception{
		String sql = "delete from Users where user_name = ? and user_mobie = ? and role = 'OPERATOR'";
		try(Connection con = BusDBUtil.getConnection();PreparedStatement ps = con.prepareStatement(sql)){
		   ps.setString(1, name);
		   ps.setString(2, mobile);
		   ps.executeUpdate();
		}
	}
	
	public void viewAllAvailableOperator() throws Exception {
		String sql = "select user_name,user_mobie from Users where role = 'OPERATOR'";
		try(Connection con = BusDBUtil.getConnection();Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql)){
			while(rs.next()) {
				System.out.println("Operator Name : "+rs.getString("user_name") + 
						" | Operator mobile : "+rs.getString("user_mobie"));
			}
		}
				
	}
	
	public void updateOperatorDetail(String updateName,String newNumber,String newPassword)throws Exception{
		String sql = "update Users set user_mobie = ?,user_password = ? where user_name = ? and role = 'OPERATOR'";
		try(Connection con = BusDBUtil.getConnection();PreparedStatement ps = con.prepareStatement(sql)){
			ps.setString(1, newNumber);
			ps.setString(2, newPassword);
			ps.setString(3, updateName);
			ps.executeUpdate();
		}
	}
	
	
	

}
