package com.model;

import com.enums.Status;

public class Product {

	private int productId;
	private String productName;
	private String description;
	private double price;
	private int categoryId;
	private Status status;
	
	public Product(String productName, String description, double price) {
		this.productName = productName;
		this.description = description;
		this.price = price;
	}

	public Product() {
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getProductId() {
		return productId;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}

	public void setProductId(int id) {
		this.productId = id;
	}
}
