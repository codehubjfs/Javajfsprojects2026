package com.ecommerce.models;

public class CartItem {
	private Product product;
    private int quantity;
    private double price;
    private double itemTotal;

    public CartItem(Product product, int quantity, double price, double itemTotal) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.itemTotal = itemTotal;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getItemTotal() {
        return itemTotal;
    }
}
