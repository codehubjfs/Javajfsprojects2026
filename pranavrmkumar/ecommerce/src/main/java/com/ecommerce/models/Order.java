package com.ecommerce.models;

import java.time.LocalDate;
import java.util.List;

public class Order {

    private int orderId;
    private int userId;
    private List<CartItem> items;
    private double totalAmount;
    private String userName;
    private LocalDate orderDate;
    private String status;
    private int address_id;

    // Constructor for DB fetch
    public Order(int orderId, List<CartItem> items,
                 double totalAmount, LocalDate orderDate,String status) {
        this.orderId = orderId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.status = status;
    }
    
    
    public Order(int orderId,int userId,String userName,double totalAmount,LocalDate orderDate,String status,int address_id) {
    	this.orderId = orderId;
    	this.userId = userId;
    	this.userName = userName;
    	this.totalAmount = totalAmount;
    	this.orderDate = orderDate;
    	this.status = status;
    	this.address_id = address_id;
    }

    public int getOrderId() {
        return orderId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void displayOrder() {
        System.out.println("\n===== ORDER SUMMARY =====");
        System.out.println("Order ID: " + orderId);
        System.out.println("Order Date: " + orderDate);
        System.out.println("Status: " + (status != null ? status.toUpperCase() : "N/A"));

        for (CartItem item : items) {
            System.out.println(
                item.getProduct().getName() + " x " +
                item.getQuantity() + " = ₹" +
                item.getPrice()
            );
        }

        System.out.println("Total Amount: ₹" + totalAmount);
        System.out.println("==========================");
    }

    public void displayAdminOrder() {
    	System.out.println("Order ID: " + orderId + 
                " | User ID: " + userId +
                " | Customer: " + userName +
                " | Date: " + orderDate +
                " | Status: " + status +
                " | Total: ₹" + totalAmount);
    }
    
    
	public int getUserId() {
		return userId;
	}


	public String getUserName() {
		return userName;
	}

	public String getStatus() {
		return status;
	}

	public int returnAddressId() {
		return address_id;
	}
}
