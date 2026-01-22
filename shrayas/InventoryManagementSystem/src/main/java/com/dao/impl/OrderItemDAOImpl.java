package com.dao.impl;

import java.sql.*;
import java.util.*;

import com.dao.OrderItemDAO;
import com.model.OrderItem;
import com.util.DatabaseConnectionPool;

public class OrderItemDAOImpl implements OrderItemDAO{

	private static final String CREATE = """
			INSERT INTO order_items (order_id, product_id, quantity, price)
			VALUES (?, ?, ?, ?)
			""";
	
	private static final String FIND_BY_ORDERID = """
			SELECT * FROM order_items WHERE order_id = ?
			"""; 
	
    public int addItem(OrderItem item) throws Exception {
    	
		try(Connection con = DatabaseConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(CREATE)){
			
			ps.setInt(1, item.getOrderId());
			ps.setInt(2, item.getProductId());
			ps.setInt(3, item.getQuantity());
			ps.setDouble(4, item.getPrice());
			
			return  ps.executeUpdate();
		}
		
    }
	
    public List<OrderItem> findByOrderId(int orderId) throws Exception {

        List<OrderItem> items = new ArrayList<>();

        try (Connection con = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_BY_ORDERID)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(mapToOrderItem(rs));
                }
            }
        }
        return items;
    }
	
	private OrderItem mapToOrderItem(ResultSet rs) throws SQLException {

		OrderItem orderItem = new OrderItem();
		orderItem.setOrderItemId(rs.getInt("order_item_id"));
		orderItem.setOrderId(rs.getInt("order_id"));
		orderItem.setProductId(rs.getInt("product_id"));
		orderItem.setQuantity(rs.getInt("quantity"));
		orderItem.setPrice(rs.getDouble("price"));
		
        return orderItem;
	}
}
