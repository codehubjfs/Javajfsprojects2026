package com.ecommerce.models;

public class Inventory {
	private int inventory_id;
	private int product_id;
	private int stock_quantity;
	private String product_name;
	
	public Inventory(int inventory_id,int product_id,int stock_quantity,String product_name) {
		this.inventory_id = inventory_id;
		this.product_id = product_id;
		this.stock_quantity = stock_quantity;
		this.product_name = product_name;
	}
	
	public int getInventoryID() {
		return inventory_id;
	}
	
	public int getProductID() {
		return product_id;
	}
	
	public int getQuantity() {
		return stock_quantity;
	}
	
	public String getPName() {
		return product_name;
	}
}
