package com.dao;

import java.util.List;

import com.enums.OrderStatus;
import com.enums.PaymentStatus;
import com.model.Order;

public interface OrderDAO {

	int createOrder(Order order) throws Exception;
	
	List<Order> findAll() throws Exception;
	
	List<Order> findByUserId(int userId) throws Exception;
	
	Order findById(int orderId) throws Exception;
	
	boolean updateOrderStatus(int orderId, OrderStatus orderStatus) throws Exception;
	
	boolean updatePaymentStatus(int orderId, PaymentStatus paymentStatus) throws Exception;
}
