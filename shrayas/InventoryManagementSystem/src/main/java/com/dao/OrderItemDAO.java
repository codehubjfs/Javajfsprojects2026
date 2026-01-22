package com.dao;

import java.util.List;

import com.model.OrderItem;

public interface OrderItemDAO {

	int addItem(OrderItem item) throws Exception;
	
	List<OrderItem> findByOrderId(int orderId) throws Exception;
}
