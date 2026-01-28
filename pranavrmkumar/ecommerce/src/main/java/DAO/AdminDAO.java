package DAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Exceptions.DBAccessException;
import Exceptions.EntityNotFoundException;
import Model.CartItem;
import Model.Category;
import Model.Customer;
import Model.Discount;
import Model.Inventory;
import Model.Order;
import Model.Product;
import Model.Ticket;
import util.DBUtil;

public class AdminDAO {
	
//Query for an Admin to view all customers
	public static ArrayList<Customer> viewCustomers() throws DBAccessException {
		ArrayList<Customer> customers = new ArrayList<>();
		String sql = "select name,email,role,status from user where role = 'customer'";
		try(Connection con = DBUtil.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql)) {
			while(rs.next()) {
				customers.add(new Customer(rs.getString("name"),rs.getString("email"),rs.getString("status")));
			}
		}
		catch(SQLException | IOException e) {
			throw new DBAccessException("Unable to fetch Customers.");
		}
		return customers;
	}
	
//Query to block a customer
	public static void blockCustomers(String email) throws DBAccessException,EntityNotFoundException{
		String sql = "update user set status = 'inactive' where email = ? and role = 'customer'";
		try(Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
			ps.setString(1, email);
			int rows = ps.executeUpdate();
			if(rows == 0) {
				throw new EntityNotFoundException("Customer not found.");
			}
		}catch(SQLException | IOException e) {
			throw new DBAccessException("Unable to fetch Customers.");
		}
	}
	
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
			throw new DBAccessException("Unable to add a new Category.");
		}
	}
	
//Query to delete a category
	public static void deleteCategory(int category_id) throws DBAccessException, EntityNotFoundException{
		String sql = "update category set status = 'inactive' where category_id = ?";
		try(Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
			ps.setInt(1, category_id);
			int rows = ps.executeUpdate();
			if(rows == 0) {
				throw new EntityNotFoundException("Category ID not found");
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
			try(Connection con = DBUtil.getConnection();
					PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setString(1, status);
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
	
//Query for Admin to add a new Product
	public static void addProduct(int category_id,String name,String brand,String description,double price,String image_url) throws DBAccessException{
		String sql1 = "insert into product(category_id,name,brand,description,price,image_url) values (?,?,?,?,?,?)";
		String sql2 = "insert into inventory(product_id,stock_quantity) values (?,0)";
		try(Connection con = DBUtil.getConnection()){
			if(!isCategoryActive(con,category_id)) {
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
		// TODO Auto-generated method stub
		String sql = "update product set status = 'inactive' where product_id = ?";
		try(Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
			ps.setInt(1, pid);
			int rows = ps.executeUpdate();
			if(rows == 0) {
				throw new EntityNotFoundException("Product not found");
			}
		}
		catch(SQLException | IOException e) {
			throw new DBAccessException("Unable to delete Product.");
		}
	}
	
	
//Query to update product price
	public static void modifyProductPrice(int pid, double newPrice) throws DBAccessException, EntityNotFoundException{
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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
	
	
//Query to view inventory
	public static ArrayList<Inventory> viewInventory() throws DBAccessException{
		ArrayList<Inventory> inventory = new ArrayList<>();
		// TODO Auto-generated method stub
		String sql = "select i.inventory_id,i.product_id,i.stock_quantity,p.name as product_name from inventory i "
				+"join product p on i.product_id = p.product_id";
		try(Connection con = DBUtil.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql)) {
			while(rs.next()) {
				inventory.add(new Inventory(rs.getInt("inventory_id"),rs.getInt("product_id"),rs.getInt("stock_quantity"),rs.getString("product_name")));
			}
		}
		catch(SQLException | IOException e) {
			throw new DBAccessException("Unable to fetch Inventory.");
		}
		return inventory;
	}
	
//Query for Admin to update the stock_quantity of a product
	public static void updateInventory(int quantity,int product_id) throws DBAccessException, EntityNotFoundException {
		String sql = "update inventory set stock_quantity = stock_quantity + ? where product_id = ?";
		try(Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
			ps.setInt(1, quantity);
			ps.setInt(2, product_id);
			int rows = ps.executeUpdate();
			if(rows == 0) {
				throw new EntityNotFoundException("Product not found");
			}
		}
		catch(SQLException | IOException e) {
			throw new DBAccessException("Unable to update Inventory.");
		}
	}
	
//Query to view all orders
	public static ArrayList<Order> viewOrders() throws DBAccessException{
		ArrayList<Order> orders = new ArrayList<>();
		String sql = "select o.order_id,o.user_id,u.name as username,o.order_date,o.status,o.total_amount,o.address_id from `order` o join user u on o.user_id = u.user_id order by o.order_date desc";
		try(Connection con = DBUtil.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql)) {
			while(rs.next()) {
				orders.add(new Order(rs.getInt("order_id"),rs.getInt("user_id"),rs.getString("username"),rs.getDouble("total_amount"),rs.getTimestamp("order_date").toLocalDateTime().toLocalDate(),rs.getString("status"),rs.getInt("address_id")));
			}
		}catch(SQLException | IOException e) {
			throw new DBAccessException("Unable to fetch Orders");
		}
		return orders;
	}
	
//Query to view order details
	public static ArrayList<CartItem> viewOrderDetails(int orderId) throws DBAccessException{
		ArrayList<CartItem> items = new ArrayList<>();
		String sql = "select p.product_id,p.name,p.brand,oi.quantity,oi.price,oi.item_total from order_item oi join product p on oi.product_id = p.product_id where oi.order_id = ?";
		try(Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
			ps.setInt(1, orderId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Product product = new Product(rs.getInt("product_id"),0,rs.getString("name"),rs.getString("brand"),rs.getDouble("price"),null,null,"active");
				items.add(new CartItem(product,rs.getInt("quantity"),rs.getDouble("price"),rs.getDouble("item_total")));
			}
		}
		catch(SQLException | IOException e) {
			throw new DBAccessException("Unable to fetch order items.");
		}
		return items;
	}
	
	
//Query for Admin to view all Payments
	
//	public static void viewPayments() throws DBAccessException{
//		String sql = "select * from payment";
//		try(Connection con = DBUtil.getConnection();
//				Statement st = con.createStatement();
//				ResultSet rs = st.executeQuery(sql)){
//			while(rs.next()) {
//				System.out.println(rs.getInt("payment_id") + " | " + rs.getInt("order_id") + " | " + rs.getString("payment_method")
//				 + " | " + rs.getString("payment_status") + " | " + rs.getString("transaction_id") + " | " + rs.getTimestamp("payment_date"));
//			}
//		}
//		catch(SQLException | IOException e) {
//			throw new DBAccessException("Unable to fetch Payments.");
//		}
//	}
	
	
//Query for Admin to Add a new discount code
	
	public static void createDiscount(String promo_code,double discount_percentage,String expiry_date) throws DBAccessException {
		String sql = "insert into discount(promo_code,discount_percentage,expiry_date) values (?,?,?)";
		try(Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
			ps.setString(1, promo_code);
			ps.setDouble(2, discount_percentage);
			ps.setDate(3, Date.valueOf(expiry_date));
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
	        if (!rs.next()) {
	            throw new DBAccessException("Failed to create Discount");
	        }
		}
		catch(SQLException | IOException e) {
			throw new DBAccessException("Unable to create Discount.");
		}
	}
	
//Query to delete a discount
	public static void deleteDiscount(int did) throws DBAccessException,EntityNotFoundException{
		// TODO Auto-generated method stub
		String sql = "update discount set status='expired' where discount_id=?";
	    try (Connection con = DBUtil.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setInt(1, did);
	        int rows = ps.executeUpdate();
	        if(rows == 0) {
				throw new EntityNotFoundException("Discount not found");
			}
		}
		catch(SQLException | IOException e) {
			throw new DBAccessException("Unable to delete Discount.");
	    }
	}
	
//Query to view all discounts
	public static ArrayList<Discount> viewDiscounts() throws DBAccessException{
		// TODO Auto-generated method stub
		ArrayList<Discount> discounts = new ArrayList<>();
		String sql1 = "update discount set status = 'expired' where expiry_date < curdate() and status = 'active'";
		String sql2 = "select * from discount";
	    try (Connection con = DBUtil.getConnection();
	         Statement st = con.createStatement()){
	         st.executeUpdate(sql1);
	         ResultSet rs = st.executeQuery(sql2);
	        while (rs.next()) {
	        	discounts.add(new Discount(rs.getInt("discount_id"),rs.getString("promo_code"),rs.getDouble("discount_percentage"),rs.getDate("expiry_date")
	        			,rs.getString("status")));
	 
	        }
	    }
	    catch(SQLException | IOException e) {
			throw new DBAccessException("Unable to fetch Discounts.");
		}
		return discounts;
	}
	
//Query for Admin to view support tickets
	
	public static ArrayList<Ticket> viewTickets() throws DBAccessException{
		ArrayList<Ticket> tickets = new ArrayList<>();
		String sql = "select * from support_ticket";
		try(Connection con = DBUtil.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql)){
			while(rs.next()) {
				tickets.add(new Ticket(rs.getInt("ticket_id"),rs.getInt("user_id"),rs.getInt("order_id"),rs.getString("issue_type"),
						rs.getString("description"),rs.getString("ticket_status"),rs.getDate("created_date")));
			}
		}
		catch(SQLException | IOException e) {
			throw new DBAccessException("Unable to fetch Tickets.");
		}
		return tickets;
	}
	
//Query for Admin to update support ticket status
	
	public static void updateTicketStatus(String ticket_status,int ticket_id) throws DBAccessException,EntityNotFoundException{
		String sql = "update support_ticket set ticket_status = ? where ticket_id = ?";
		try(Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
			ps.setString(1, ticket_status);
			ps.setInt(2, ticket_id);
			int rows = ps.executeUpdate();
			if(rows == 0) {
				throw new EntityNotFoundException("Ticket not found");
			}
		}
		catch(SQLException | IOException e) {
			throw new DBAccessException("Unable to update Ticket.");
		}
	}
}
