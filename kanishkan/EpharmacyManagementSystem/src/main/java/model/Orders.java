package model;
import enums.*;

public class Orders {
	
	int orderId;
	String orderDate;
	double totalAmount;
	String deliveryAddress;
	OrderStatus orderStatus;
	PaymentStatus paymentStatus;
	int userId;
	
	public Orders(int orderId, String orderDate, double totalAmount, String deliveryAddress, OrderStatus orderStatus,
			PaymentStatus paymentStatus, int userId) {
		this.orderId = orderId;
		this.orderDate = orderDate;
		this.totalAmount = totalAmount;
		this.deliveryAddress = deliveryAddress;
		this.orderStatus = orderStatus;
		this.paymentStatus = paymentStatus;
		this.userId = userId;
	}

}
