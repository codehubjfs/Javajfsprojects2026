package com.ecommerce.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.ecommerce.exceptions.DBAccessException;
import com.ecommerce.exceptions.EntityNotFoundException;
import com.ecommerce.models.CartItem;
import com.ecommerce.models.Customer;
import com.ecommerce.models.Discount;
import com.ecommerce.models.Inventory;
import com.ecommerce.models.Order;
import com.ecommerce.models.Payment;
import com.ecommerce.models.Product;
import com.ecommerce.models.Ticket;
import com.ecommerce.util.DBUtil;

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
	
	public static List<Payment> viewPayments() throws DBAccessException{
		List<Payment> payments = new ArrayList<>();
		String sql = "select * from payment";
		try(Connection con = DBUtil.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql)){
			while(rs.next()) {
				payments.add(new Payment(rs.getInt("payment_id"),rs.getInt("order_id"),rs.getString("payment_method"),rs.getString("payment_status"),rs.getString("transaction_id"),rs.getTimestamp("payment_date")));
			}
		}
		catch(SQLException | IOException e) {
			throw new DBAccessException("Unable to fetch Payments.");
		}
		return payments;
	}
	
	
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
