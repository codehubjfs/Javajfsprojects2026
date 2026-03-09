package com.ecommerce.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.ecommerce.exceptions.DBAccessException;
import com.ecommerce.exceptions.EntityNotFoundException;
import com.ecommerce.models.Category;
import com.ecommerce.util.DBUtil;

public class CategoryDAO {

	//Query to view all categories
		public static ArrayList<Category> viewCategories() throws DBAccessException{
			ArrayList<Category> categories = new ArrayList<>();
			String sql = "select * from category";
			try(Connection con = DBUtil.getConnection();
					Statement st = con.createStatement();
					ResultSet rs = st.executeQuery(sql)) {
				while(rs.next()) {
					categories.add(new Category(rs.getInt("category_id"),rs.getString("category_name"),rs.getString("description"),rs.getString("status")));
				}
			}
			catch(SQLException | IOException e) {
				throw new DBAccessException("Unable to fetch Categories.");
			}
			return categories;
		}
		
		
	//Query for Admin to Add a new Category
		public static void addCategory(String category_name,String description) throws DBAccessException {
			String sql = "insert into category(category_name,description) values (?,?)";
			try(Connection con = DBUtil.getConnection();
					PreparedStatement ps = con.prepareStatement(sql)){
				ps.setString(1, category_name);
				ps.setString(2, description);
				int rows = ps.executeUpdate();
		        if (rows == 0) {
		            throw new DBAccessException("Failed to create Category");
		        }
			}
			catch(SQLException | IOException e) {
				if (((SQLException) e).getErrorCode() == 1062) {
					throw new DBAccessException("Category already exists."); 
					}
				throw new DBAccessException("Unable to add a new Category.");
			}
		}
		
	//Query to delete a category
		public static void deleteCategory(int category_id) throws DBAccessException, EntityNotFoundException{
			String sql = "update category set status = 'inactive' where category_id = ?";
			String sql1 = "update product set status = 'inactive' where category_id = ?";
			try(Connection con = DBUtil.getConnection()){
					con.setAutoCommit(false);
					try(PreparedStatement ps = con.prepareStatement(sql);
					PreparedStatement ps1 = con.prepareStatement(sql1)){
				ps.setInt(1, category_id);
				int rows = ps.executeUpdate();
				if(rows == 0) {
					con.rollback();
					throw new EntityNotFoundException("Category ID not found");
				}
				ps1.setInt(1,category_id);
				ps1.executeUpdate();
				con.commit();
				}
			}
			catch(SQLException | IOException e) {
				throw new DBAccessException("Unable to delete the Category.");
			}
		}
		
	//Query to modify category name
		public static void modifyCategoryName(String category_name,int category_id) throws DBAccessException, EntityNotFoundException{
			String sql = "update category set category_name = ? where category_id = ?";
			try(Connection con = DBUtil.getConnection();
					PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setString(1, category_name);
				ps.setInt(2, category_id);
				int rows = ps.executeUpdate();
				if(rows == 0) {
					throw new EntityNotFoundException("Category ID not found");
				}
			}
			catch(SQLException | IOException e) {
				throw new DBAccessException("Unable to modify Category.");
			}
		}
		
	//Query to modify category desc
		public static void modifyCategoryDesc(String description,int category_id) throws DBAccessException, EntityNotFoundException{
			String sql = "update category set description = ? where category_id = ?";
			try(Connection con = DBUtil.getConnection();
					PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setString(1, description);
				ps.setInt(2, category_id);
				int rows = ps.executeUpdate();
				if(rows == 0) {
					throw new EntityNotFoundException("Category ID not found");
				}
			}
			catch(SQLException | IOException e) {
				throw new DBAccessException("Unable to modify Category.");
			}
		}
			
	//Query to modify category status
			public static void modifyCategoryStatus(String status,int category_id) throws DBAccessException, EntityNotFoundException{
				String sql = "update category set status = ? where category_id = ?";
				String sql1 = "update product set status = ? where category_id = ?";
				try(Connection con = DBUtil.getConnection()){
					con.setAutoCommit(false);
						try(PreparedStatement ps = con.prepareStatement(sql);
								PreparedStatement ps1 = con.prepareStatement(sql1)){
					ps.setString(1, status);
					ps.setInt(2, category_id);
					int rows = ps.executeUpdate();
					if(rows == 0) {
						con.rollback();
						throw new EntityNotFoundException("Category ID not found");
					}
					ps1.setString(1,status);
					ps1.setInt(2,category_id);
					ps1.executeUpdate();
					con.commit();
				}
				}
				catch(SQLException | IOException e) {
					throw new DBAccessException("Unable to modify Category.");
				}
			}
			
			
	//Query to check if Category is active and exists
			public static boolean isCategoryActive(Connection con,int category_id) throws SQLException{
				String sql = "select status from category where category_id = ?";
				try(PreparedStatement ps = con.prepareStatement(sql)){
					ps.setInt(1, category_id);
					ResultSet rs = ps.executeQuery();
					if(rs.next()) {
						return "active".equalsIgnoreCase(rs.getString("status"));
					}
					return false;
				}
			}
}
