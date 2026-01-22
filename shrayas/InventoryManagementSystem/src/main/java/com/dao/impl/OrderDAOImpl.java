package com.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dao.OrderDAO;
import com.enums.OrderStatus;
import com.enums.PaymentStatus;
import com.model.Order;
import com.util.DatabaseConnectionPool;
import com.util.DateUtil;

public class OrderDAOImpl implements OrderDAO{

	private static final String CREATE = """
			INSERT INTO orders (user_id, total_amount)
			VALUES (?, ?)
			""";
	
	private static final String FIND_BY_ID = """
			SELECT * FROM orders WHERE order_id = ?
			"""; 
	
	private static final String FIND_BY_USERID = """
			SELECT * FROM orders WHERE user	_id = ?
			""";
	
	private static final String FIND_ALL = """
			SELECT * FROM orders 
			""";
	
	private static final String UPDATE_ORDER_STATUS = """
			UPDATE orders SET order_status = ?
			WHERE order_id  = ? 
			""";
	
	private static final String UPDATE_PAYMENT_STATUS = """
			UPDATE orders SET payment_status = ?
			WHERE order_id  = ? 
			""";
	

	public int createOrder(Order order) throws Exception{
		
		try(Connection con = DatabaseConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(CREATE)){
			
			ps.setInt(1, order.getUserId());
			ps.setDouble(2, order.getTotalAmount());
			
			return ps.executeUpdate();
		}
		
	}
	
	public List<Order> findAll() throws Exception {
		
		List<Order> orders = new ArrayList<>();
    	try(Connection con = DatabaseConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(FIND_ALL)){
    		
    		try (ResultSet rs = ps.executeQuery()){
    			while (rs.next()) {
    				orders.add(mapToOrder(rs));
    			}
    		}
    	}
    return orders;
	}
	
	public Order findById(int orderId) throws Exception {
		
		try(Connection con = DatabaseConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(FIND_BY_ID)){
			
			ps.setInt(1, orderId);
			
			try (ResultSet rs = ps.executeQuery()){
				if (rs.next()) {
					return mapToOrder(rs);
				}
			}
    	}

    	return null;
	}
	
	public Order findByUserId(int userId) throws Exception {
			
			try(Connection con = DatabaseConnectionPool.getConnection();
					PreparedStatement ps = con.prepareStatement(FIND_BY_USERID)){
				
				ps.setInt(1, userId);
				
				try (ResultSet rs = ps.executeQuery()){
					if (rs.next()) {
						return mapToOrder(rs);
					}
				}
	    	}
	
	    	return null;
		}
	
	public boolean updateOrderStatus(int orderId, OrderStatus orderStatus) throws Exception{
		
		try(Connection con = DatabaseConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(UPDATE_ORDER_STATUS)){
			
			ps.setString(1, orderStatus.name());
			ps.setInt(2, orderId);
			
			return ps.executeUpdate() > 0;
    	}
	}
	
	public boolean updatePaymentStatus(int orderId, PaymentStatus paymentStatus) throws Exception {
		
		try(Connection con = DatabaseConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(UPDATE_PAYMENT_STATUS)){
			
			ps.setString(1, paymentStatus.name());
			ps.setInt(2, orderId);
			
			return ps.executeUpdate() > 0;
		}

	}
	
	private Order mapToOrder(ResultSet rs) throws SQLException {

        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setUserId(rs.getInt("user_id"));
        order.setOrderStatus(OrderStatus.valueOf(rs.getString("order_status")));
        order.setPaymentStatus(PaymentStatus.valueOf(rs.getString("payment_status")));
        order.setCreatedAt(DateUtil.toLocalDateTime(rs.getTimestamp("created_at")));
        order.setUpdatedAt(DateUtil.toLocalDateTime(rs.getTimestamp("updated_at")));
     
        
        return order;
    }

}
