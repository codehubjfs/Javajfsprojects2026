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
import com.ecommerce.models.Product;
import com.ecommerce.util.DBUtil;

public class ProductDAO {

	//Query for Admin to add a new Product
		public static void addProduct(int category_id,String name,String brand,String description,double price,String image_url) throws DBAccessException{
			String sql1 = "insert into product(category_id,name,brand,description,price,image_url) values (?,?,?,?,?,?)";
			String sql2 = "insert into inventory(product_id,stock_quantity) values (?,0)";
			try(Connection con = DBUtil.getConnection()){
				if(!CategoryDAO.isCategoryActive(con,category_id)) {
					throw new DBAccessException("Category does not exist or is inactive");
				}
				con.setAutoCommit(false);
				PreparedStatement ps = con.prepareStatement(sql1,Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, category_id);
				ps.setString(2, name);
				ps.setString(3, brand);
				ps.setString(4, description);
				ps.setDouble(5, price);
				ps.setString(6, image_url);
				ps.executeUpdate();
				
				ResultSet rs = ps.getGeneratedKeys();
		        if (!rs.next()) {
		            throw new DBAccessException("Failed to create product");
		        }

		        int productId = rs.getInt(1);
		        PreparedStatement ps2 = con.prepareStatement(sql2);
		        ps2.setInt(1, productId);
		        ps2.executeUpdate();
		        con.commit();
			}
			catch(SQLException | IOException e) {
				throw new DBAccessException("Unable to create Product.");
			}
		}
		
	//Query to delete a Product
		public static void deleteProduct(int pid) throws DBAccessException ,EntityNotFoundException{
			String sql = "update product set status = 'inactive' where product_id = ?";
			String sql1 = "delete from cart_item where product_id = ?";
			try(Connection con = DBUtil.getConnection()){
					con.setAutoCommit(false);
					try(PreparedStatement ps = con.prepareStatement(sql);
					PreparedStatement ps1 = con.prepareStatement(sql1)){
				ps.setInt(1, pid);
				int rows = ps.executeUpdate();
				
				if(rows == 0) {
					con.rollback();
					throw new EntityNotFoundException("Product not found");
				}
				ps1.setInt(1, pid);
				ps1.executeUpdate();
				con.commit();
			}
			}catch(SQLException | IOException e) {
				throw new DBAccessException("Unable to delete Product.");
			}
		}
		
		
	//Query to update product price
		public static void modifyProductPrice(int pid, double newPrice) throws DBAccessException, EntityNotFoundException{
			String sql = "update product set price = ? where product_id = ?";
			try(Connection con = DBUtil.getConnection();
					PreparedStatement ps = con.prepareStatement(sql)){
				ps.setDouble(1, newPrice);
				ps.setInt(2, pid);
				int rows = ps.executeUpdate();
				if(rows == 0) {
					throw new EntityNotFoundException("Product not found");
				}
			}
			catch(SQLException | IOException e) {
				throw new DBAccessException("Unable to modify Product.");
			}
		}
		
	//Query to view all products
		public static ArrayList<Product> viewProducts() throws DBAccessException {
			ArrayList<Product> products = new ArrayList<>();
			String sql = "select * from product";
			try(Connection con = DBUtil.getConnection();
					Statement st = con.createStatement();
					ResultSet rs = st.executeQuery(sql)) {
				while(rs.next()) {
					products.add(new Product(rs.getInt("product_id"),rs.getInt("category_id"),rs.getString("name"),rs.getString("brand"),
							rs.getDouble("price"),rs.getString("description"),rs.getString("image_url"),rs.getString("status")));
				}
			}
			catch(SQLException | IOException e) {
				throw new DBAccessException("Unable to fetch Products.");
			}
			return products;
		}
		
		//View products
		public static ArrayList<Product> getActiveProducts() throws DBAccessException {
	        ArrayList<Product> products = new ArrayList<>();

	        String sql = "select * from product where status = 'active'";

	        try (Connection con = DBUtil.getConnection();
	             Statement st = con.createStatement();
	             ResultSet rs = st.executeQuery(sql)) {

	            while (rs.next()) {
	                products.add(new Product(
	                        rs.getInt("product_id"),
	                        rs.getInt("category_id"),
	                        rs.getString("name"),
	                        rs.getString("brand"),
	                        rs.getDouble("price"),
	                        rs.getString("description"),
	                        rs.getString("image_url"),
	                        rs.getString("status")
	                ));
	            }
	        } catch (SQLException | IOException e) {
	            throw new DBAccessException("Unable to fetch products.");
	        }

	        return products;
		}
		
		public static Product getProductById(int productId) throws DBAccessException {

		    String sql = "select * from product where product_id = ? and status='active'";

		    try (Connection con = DBUtil.getConnection();
		         PreparedStatement ps = con.prepareStatement(sql)) {

		        ps.setInt(1, productId);
		        ResultSet rs = ps.executeQuery();

		        if (rs.next()) {
		            return new Product(
		                    rs.getInt("product_id"),
		                    rs.getInt("category_id"),
		                    rs.getString("name"),
		                    rs.getString("brand"),
		                    rs.getDouble("price"),
		                    rs.getString("description"),
		                    rs.getString("image_url"),
		                    rs.getString("status")
		            );
		        }
		        return null;

		    } catch (SQLException | IOException e) {
		        throw new DBAccessException("Unable to fetch product.");
		    }
		}

		public static boolean isProductActive(int productID) throws DBAccessException {
			String sql = "SELECT status FROM product WHERE product_id = ?"; 
			try (Connection con = DBUtil.getConnection(); 
					PreparedStatement ps = con.prepareStatement(sql)) { 
				ps.setInt(1, productID); 
				ResultSet rs = ps.executeQuery(); 
				if (rs.next()) { 
					return "active".equalsIgnoreCase(rs.getString("status")); 
				} return false; 
			} catch (SQLException | IOException e) { 
				throw new DBAccessException("Unable to check product status."); 
			}
		}
}
