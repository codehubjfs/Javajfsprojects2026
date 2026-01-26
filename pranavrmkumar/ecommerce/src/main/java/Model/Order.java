package Model;

import java.time.LocalDate;
import java.util.List;

public class Order {

    private int orderId;
    private List<CartItem> items;
    private double totalAmount;
    private LocalDate orderDate;

    // Constructor for DB fetch
    public Order(int orderId, List<CartItem> items,
                 double totalAmount, LocalDate orderDate) {
        this.orderId = orderId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
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
}
