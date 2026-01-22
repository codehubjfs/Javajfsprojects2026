package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class AdminDAO {
	
//Query for an Admin to view all customers
	public static void viewCustomers() throws Exception {
		String sql = "select user_id,name,email from user where role = 'customer'";
		try(Connection con = DBUtil.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql)) {
			while(rs.next()) {
				System.out.println(rs.getInt("user_id") + " | " +
						rs.getString("name") + " | " +
						rs.getString("email") + " | ");
			}
		}
	}
	
//Query to view all categories
	public static void viewCategories() throws Exception {
		String sql = "select * from category";
		try(Connection con = DBUtil.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql)) {
			while(rs.next()) {
				System.out.println(rs.getInt("category_id") + " | " + rs.getString("category_name") + " | " + rs.getString("description")
				+ " | " + rs.getString("status"));
			}
		}
	}
	
	
//Query for Admin to Add a new Category
	public static void addCategory(String category_name,String description) throws Exception {
		String sql = "insert into category(category_name,description) values (?,?)";
		try(Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
			ps.setString(1, category_name);
			ps.setString(2, description);
			ps.executeUpdate();
		}
	}
	
//Query to delete a category
	public static void deleteCategory(String category_name) throws Exception{
		String sql = "update category set status = 'inactive' where category_name = ?";
		try(Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
			ps.setString(1, category_name);
			ps.executeUpdate();
		}
	}
	
//Query to modify category name
	public static void modifyCategoryName(String category_name,int category_id) throws Exception{
		String sql = "update category set category_name = ? where category_id = ?";
		try(Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, category_name);
			ps.setInt(2, category_id);
			ps.executeUpdate();
		}
	}
	
//Query to modify category desc
	public static void modifyCategoryDesc(String description,int category_id) throws Exception{
		String sql = "update category set description = ? where category_id = ?";
		try(Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, description);
			ps.setInt(2, category_id);
			ps.executeUpdate();
		}
	}
		
//Query to modify category status
		public static void modifyCategoryStatus(String status,int category_id) throws Exception{
			String sql = "update category set status = ? where category_id = ?";
			try(Connection con = DBUtil.getConnection();
					PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setString(1, status);
				ps.setInt(2, category_id);
				ps.executeUpdate();
			}
		}
	
//Query for Admin to add a new Product
	public static void addProduct(int category_id,String name,String brand,String description,double price,String image_url) throws Exception{
		String sql = "insert into product(category_id,name,brand,description,price,image_url) values (?,?,?,?,?,?)";
		try(Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
			ps.setInt(1, category_id);
			ps.setString(2, name);
			ps.setString(3, brand);
			ps.setString(4, description);
			ps.setDouble(5, price);
			ps.setString(6, image_url);
			ps.executeUpdate();
		}
	}
	
//Query to delete a Product
	public static void deleteProduct(int pid) throws Exception {
		// TODO Auto-generated method stub
		String sql = "update product set status = 'inactive' where product_id = ?";
		try(Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
			ps.setInt(1, pid);
			ps.executeUpdate();
		}
	}
	
	
//Query to update product price
	public static void modifyProductPrice(int pid, double newPrice) throws Exception {
		// TODO Auto-generated method stub
		String sql = "update product set price = ? where product_id = ?";
		try(Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
			ps.setDouble(1, newPrice);
			ps.setInt(2, pid);
			ps.executeUpdate();
		}
	}
	
//Query to view all products
	public static void viewProducts() throws Exception {
		// TODO Auto-generated method stub
		String sql = "select * from product";
		try(Connection con = DBUtil.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql)) {
			while(rs.next()) {
				System.out.println(rs.getInt("product_id") + " | " + rs.getInt("category_id") + " | " + rs.getString("name") + " | " + 
			rs.getString("brand") + " | " + rs.getDouble("price") + " | " + rs.getString("description") + " | " + rs.getString("image_url") + " | " + rs.getString("status"));
			}
		}
	}
	
	
//Query to view inventory
	public static void viewInventory() throws Exception{
		// TODO Auto-generated method stub
		String sql = "select * from inventory";
		try(Connection con = DBUtil.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql)) {
			while(rs.next()) {
				System.out.println(rs.getInt("inventory_id") + " | " + rs.getInt("product_id") + " | " + rs.getInt("stock_quantity"));
			}
		}
	}
	
//Query for Admin to update the stock_quantity of a product
	public static void updateInventory(int quantity,int product_id) throws Exception {
		String sql = "update inventory set stock_quantity = stock_quantity + ? where product_id = ?";
		try(Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
			ps.setInt(1, quantity);
			ps.setInt(2, product_id);
			ps.executeUpdate();
		}
	}
	
//Query for Admin to view all Orders
	
	public static void viewOrders() throws Exception{
		String sql = "select * from `order`";
		try(Connection con = DBUtil.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql)){
			while(rs.next()) {
				System.out.println(rs.getInt("order_id") + " | " + rs.getInt("user_id") + " | " + 
						rs.getTimestamp("order_date") + " | " + rs.getString("status") + " | " + rs.getDouble("total_amount"));
			}
		}
	}
	
	
//Query for Admin to view all Payments
	
	public static void viewPayments() throws Exception{
		String sql = "select * from payment";
		try(Connection con = DBUtil.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql)){
			while(rs.next()) {
				System.out.println(rs.getInt("payment_id") + " | " + rs.getInt("order_id") + " | " + rs.getString("payment_method")
				 + " | " + rs.getString("payment_status") + " | " + rs.getString("transaction_id") + " | " + rs.getTimestamp("payment_date"));
			}
		}
	}
	
	
//Query for Admin to Add a new discount code
	
	public static void createDiscount(String promo_code,double discount_percentage,String expiry_date) throws Exception {
		String sql = "insert into discount(promo_code,discount_percentage,expiry_date) values (?,?,?)";
		try(Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
			ps.setString(1, promo_code);
			ps.setDouble(2, discount_percentage);
			ps.setDate(3, Date.valueOf(expiry_date));
			ps.executeUpdate();
		}
	}
	
//Query to delete a discount
	public static void deleteDiscount(int did) throws Exception{
		// TODO Auto-generated method stub
		String sql = "update discount set status='expired' where discount_id=?";
	    try (Connection con = DBUtil.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setInt(1, did);
	        ps.executeUpdate();
	    }
	}
	
//Query to view all discounts
	public static void viewDiscounts() throws Exception{
		// TODO Auto-generated method stub
		String sql = "select * from discount";
	    try (Connection con = DBUtil.getConnection();
	         Statement st = con.createStatement();
	         ResultSet rs = st.executeQuery(sql)) {
	        while (rs.next()) {
	            System.out.println(rs.getInt("discount_id") + " | " +
	                               rs.getString("promo_code"));
	        }
	    }
	}
	
//Query for Admin to view support tickets
	
	public static void viewTickets() throws Exception{
		String sql = "select * from support_ticket";
		try(Connection con = DBUtil.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql)){
			while(rs.next()) {
				System.out.println(rs.getInt("ticket_id") + " | " + rs.getInt("user_id") + " | " + rs.getInt("order_id") + " | " + rs.getString("subject")
				 + " | " + rs.getString("issue_type")+ " | " + rs.getString("description") + " | " + rs.getString("status") + " | " + rs.getTimestamp("created_date"));
			}
		}
	}
	
//Query for Admin to update support ticket status
	
	public static void updateTicketStatus(String ticket_status,int ticket_id) throws Exception{
		String sql = "update support_ticket set ticket_status = ? where ticket_id = ?";
		try(Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
			ps.setString(1, ticket_status);
			ps.setInt(2, ticket_id);
			ps.executeUpdate();
		}
	}

	

	

	
}
