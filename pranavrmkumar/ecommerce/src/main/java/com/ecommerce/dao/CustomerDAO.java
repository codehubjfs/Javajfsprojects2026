package com.ecommerce.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ecommerce.exceptions.DBAccessException;
import com.ecommerce.exceptions.EntityNotFoundException;
import com.ecommerce.models.CartItem;
import com.ecommerce.models.Discount;
import com.ecommerce.models.Order;
import com.ecommerce.models.Product;
import com.ecommerce.util.DBUtil;

public class CustomerDAO {


	
	
//cart for the customer

	public static int getOrCreateCart(String email) throws DBAccessException {

	    int userId = UserDAO.getUserIdByEmail(email);

	    String checkSql = "select cart_id from cart where user_id=?";

	    try (Connection con = DBUtil.getConnection();
	         PreparedStatement ps = con.prepareStatement(checkSql)) {

	        ps.setInt(1, userId);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            return rs.getInt("cart_id");
	        }

	        String insertSql = "insert into cart(user_id,total_amount) values (?,0)";
	        try (PreparedStatement insertPs =
	                     con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

	            insertPs.setInt(1, userId);
	            insertPs.executeUpdate();

	            ResultSet keys = insertPs.getGeneratedKeys();
	            if (keys.next()) {
	                return keys.getInt(1);
	            }
	        }

	    } catch (SQLException | IOException e) {
	        throw new DBAccessException("Unable to create cart.");
	    }
	    throw new DBAccessException("Failed to create or retrieve cart.");
	}



	
//Add to cart
	public static void addOrUpdateCartItem(int cartId, Product product, int quantity)
	        throws DBAccessException {

		String updateCartTotalSql = "update cart set total_amount = (select sum(item_total) from cart_item where cart_id = ?) where cart_id = ?";
	    String checkSql =
	            "select quantity from cart_item where cart_id=? and product_id=?";

	    try (Connection con = DBUtil.getConnection();
	         PreparedStatement checkPs = con.prepareStatement(checkSql)) {

	        checkPs.setInt(1, cartId);
	        checkPs.setInt(2, product.getProductID());
	        ResultSet rs = checkPs.executeQuery();

	        if (rs.next()) {
	            String updateSql =
	                "update cart_item "
	                + "set quantity = quantity + ?,"
	                + "price = ?, "
	                + "item_total = (quantity +?) * ? "
	                + "where cart_id=? and product_id=?";

	            try (PreparedStatement ps = con.prepareStatement(updateSql)) {
	                ps.setInt(1, quantity);
	                ps.setDouble(2, product.getPrice());
	                ps.setInt(3, quantity);
	                ps.setDouble(4, product.getPrice());
	                ps.setInt(5, cartId);
	                ps.setInt(6, product.getProductID());
	                ps.executeUpdate();
	            }

	        } else {
	            String insertSql =
	                "insert into cart_item(cart_id, product_id, quantity, price, item_total) values (?,?,?,?,?)";

	            try (PreparedStatement ps = con.prepareStatement(insertSql)) {
	                ps.setInt(1, cartId);
	                ps.setInt(2, product.getProductID());
	                ps.setInt(3, quantity);
	                ps.setDouble(4, product.getPrice());
	                ps.setDouble(5, product.getPrice() * quantity);
	                ps.executeUpdate();
	            }
	        }
	        try (PreparedStatement totalPs = con.prepareStatement(updateCartTotalSql)) {
	            totalPs.setInt(1, cartId);
	            totalPs.setInt(2, cartId);
	            totalPs.executeUpdate();
	        }

	    } catch (SQLException | IOException e) {
	        throw new DBAccessException("Unable to add item to cart.");
	    }
	}



	
	
//to return stock
	public static int getStock(int productId) throws DBAccessException {

	    String sql = "select stock_quantity from inventory where product_id=?";

	    try (Connection con = DBUtil.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, productId);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            return rs.getInt("stock_quantity");
	        }
	        return 0;

	    } catch (SQLException | IOException e) {
	        throw new DBAccessException("Unable to fetch stock.");
	    }
	}


	
	
//to view the cart
	public static ArrayList<CartItem> viewCart(int cartId) throws DBAccessException {

	    ArrayList<CartItem> cartItems = new ArrayList<>();

	    String sql = "select p.*, ci.quantity, ci.price, ci.item_total from cart_item ci join product p on ci.product_id = p.product_id where ci.cart_id = ?";

	    try (Connection con = DBUtil.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, cartId);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            Product product = new Product(
	                    rs.getInt("product_id"),
	                    rs.getInt("category_id"),
	                    rs.getString("name"),
	                    rs.getString("brand"),
	                    rs.getDouble("price"),
	                    rs.getString("description"),
	                    rs.getString("image_url"),
	                    rs.getString("status")
	            );

	            CartItem item = new CartItem(
	                    product,
	                    rs.getInt("quantity"),
	                    rs.getDouble("price"),
	                    rs.getDouble("item_total")
	            );

	            cartItems.add(item);
	        }

	    } catch (SQLException | IOException e) {
	        throw new DBAccessException("Unable to fetch cart.");
	    }
	    return cartItems;
	}


	
//to create an order/checkout
	public static int createOrder(String email,ArrayList<CartItem> items,double total,int address_id)
            		throws DBAccessException {
		String getUserSql = "select user_id from user where email = ? and status = 'active'";
	    String getCartSql = "select cart_id from cart where user_id = ?";
		String orderSql =
				"insert into `order`(user_id, total_amount, status, address_id) values (?, ?, 'placed', ?)";

		String orderItemSql =
				"insert into order_item(order_id, product_id, quantity, price, item_total) values (?, ?, ?, ?, ?)";

		String stockUpdateSql =
				"update inventory set stock_quantity = stock_quantity - ? where product_id = ?";

		String clearCartSql =
				"delete from cart_item where cart_id = ?";

		try (Connection con = DBUtil.getConnection()) {
			con.setAutoCommit(false);
			
			int userId;
			try(PreparedStatement ps = con.prepareStatement(getUserSql)){
				ps.setString(1, email);
				ResultSet rs = ps.executeQuery();
				if(!rs.next()) {
					con.rollback();
					throw new DBAccessException("User not found.");
				}
				userId = rs.getInt("user_id");
			}
			int cartId;
			try(PreparedStatement ps = con.prepareStatement(getCartSql)){
				ps.setInt(1, userId);
				ResultSet rs = ps.executeQuery();
				if(!rs.next()) {
					con.rollback();
					throw new DBAccessException("Cart not found.");
				}
				cartId = rs.getInt("cart_id");
			}

			String stockCheckSql = "select stock_quantity from inventory where product_id = ?";
			for (CartItem item : items) {
			    try (PreparedStatement stockCheck = con.prepareStatement(stockCheckSql)) {
			        stockCheck.setInt(1, item.getProduct().getProductID());
			        ResultSet stockRs = stockCheck.executeQuery();
			        if (stockRs.next()) {
			            int available = stockRs.getInt("stock_quantity");
			            if (available < item.getQuantity()) {
			                con.rollback();
			                throw new DBAccessException("Insufficient stock for: "
			                    + item.getProduct().getName()
			                    + ". Available: " + available);
			            }
			        } else {
			            con.rollback();
			            throw new DBAccessException("Product not in inventory: "
			                + item.getProduct().getName());
			        }
			    }
			}
			PreparedStatement orderPs =
					con.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);

			orderPs.setInt(1, userId);
			orderPs.setDouble(2, total);
			orderPs.setInt(3, address_id);
			orderPs.executeUpdate();

			ResultSet rs = orderPs.getGeneratedKeys();
			if(!rs.next()) {
				con.rollback();
				throw new DBAccessException("Failed to create order");
			}
			int orderId = rs.getInt(1);

			for (CartItem item : items) {

				PreparedStatement itemPs = con.prepareStatement(orderItemSql);
				itemPs.setInt(1, orderId);
				itemPs.setInt(2, item.getProduct().getProductID());
				itemPs.setInt(3, item.getQuantity());
				itemPs.setDouble(4, item.getPrice());
				itemPs.setDouble(5, item.getItemTotal());
				itemPs.executeUpdate();

				PreparedStatement stockPs = con.prepareStatement(stockUpdateSql);
				stockPs.setInt(1, item.getQuantity());
				stockPs.setInt(2, item.getProduct().getProductID());
				stockPs.executeUpdate();
			}

			PreparedStatement clearPs = con.prepareStatement(clearCartSql);
			clearPs.setInt(1, cartId);
			clearPs.executeUpdate();

			con.commit(); 
			return orderId;

		} catch (SQLException | IOException e) {
			throw new DBAccessException("Order placement failed.");
		}
	}
	
	
	
//to return list of orders
	public static List<Order> getOrders(String email)
	        throws DBAccessException {

	    List<Order> orders = new ArrayList<>();

	    int userId = UserDAO.getUserIdByEmail(email);

	    String sql =
	        "select o.order_id, o.order_date, o.total_amount,o.status,p.product_id, p.name, p.price,oi.quantity from `order` o "
	        + "join order_item oi on o.order_id = oi.order_id "
	        + "join product p on oi.product_id = p.product_id where o.user_id = ? order by o.order_date desc";

	    try (Connection con = DBUtil.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, userId);
	        ResultSet rs = ps.executeQuery();

	        Map<Integer, List<CartItem>> orderItemsMap = new LinkedHashMap<>();
	        Map<Integer, OrderData> orderDataMap = new LinkedHashMap<>();

	        while (rs.next()) {

	            int orderId = rs.getInt("order_id");

	            orderDataMap.putIfAbsent(orderId,
	                new OrderData(
	                    rs.getDouble("total_amount"),
	                    rs.getTimestamp("order_date").toLocalDateTime().toLocalDate(),
	                    rs.getString("status")
	                )
	            );

	            Product product = new Product(
	                rs.getInt("product_id"),
	                0,
	                rs.getString("name"),
	                null,
	                rs.getDouble("price"),
	                null,
	                null,
	                "active"
	            );

	            int quantity = rs.getInt("quantity");
	            double price = rs.getDouble("price");

	            CartItem item = new CartItem(
	                product,
	                quantity,
	                price,
	                price * quantity
	            );

	            
	            orderItemsMap
	                .computeIfAbsent(orderId, k -> new ArrayList<>())
	                .add(item);
	        }

	        for (int orderId : orderItemsMap.keySet()) {
	            OrderData data = orderDataMap.get(orderId);
	            orders.add(new Order(
	                orderId,
	                orderItemsMap.get(orderId),
	                data.totalAmount,
	                data.orderDate,
	                data.status
	            ));
	        }

	    } catch (SQLException | IOException e) {
	        throw new DBAccessException("Unable to fetch orders.");
	    }

	    return orders;
	}


	static class OrderData {
	    double totalAmount;
	    LocalDate orderDate;
	    String status;

	    OrderData(double totalAmount, LocalDate orderDate,String status) {
	        this.totalAmount = totalAmount;
	        this.orderDate = orderDate;
	        this.status = status;
	    }
	}


	
	
	//create a payment
	public static void createPayment(int orderId,String method,String status,String txnId) throws DBAccessException{
		String sql = "insert into payment(order_id,payment_method,payment_status,transaction_id) values (?,?,?,?)";
		try(Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
			ps.setInt(1, orderId);
			ps.setString(2, method);
			ps.setString(3, status);
			ps.setString(4, txnId);
			ps.executeUpdate();
		}catch(SQLException | IOException e) {
			throw new DBAccessException("Unable to create payment");
		}
	}
	
	
	
	//view all discounts
	public static List<Discount> getActiveDiscounts() throws DBAccessException{
		List<Discount> discounts = new ArrayList<>();
		String sql = "select * from discount where status = 'active' and expiry_date >= CURDATE()";
		try(Connection con = DBUtil.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql)){
			while(rs.next()) {
				discounts.add(new Discount(
						rs.getInt("discount_id"),rs.getString("promo_code"),rs.getDouble("discount_percentage"),rs.getDate("expiry_date"),rs.getString("status")));
			}
		}catch(SQLException | IOException e) {
			throw new DBAccessException("Unable to fetch discounts.");
		}
		return discounts;
	}
	
	
	
	//apply discount to total
	public static double applyDiscount(String promoCode,double total) throws DBAccessException{
		String sql = "select discount_percentage from discount where promo_code = ? and status = 'active' and expiry_date >= CURDATE()";
		try(Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, promoCode);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				double percent = rs.getDouble("discount_percentage");
				double discountedTotal = total - (total * percent/100.0);
				return discountedTotal;
			}
			else {
				throw new DBAccessException("Invalid or expired promo code.");
			}
		}catch(SQLException | IOException e) {
			throw new DBAccessException("Unable to apply discount.");
		}
	}

	
	//create and submit a support ticket
	public static void createSupportTicket(int userId, Integer orderId, String issueType, String description) throws DBAccessException {
		String sql = "insert into support_ticket(user_id, order_id, issue_type, description) values (?, ?, ?, ?)";
		try (Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setInt(1, userId);
				if (orderId != null) {
					ps.setInt(2, orderId);
				} 
				else {
					ps.setNull(2, java.sql.Types.INTEGER);
				}
				ps.setString(3, issueType);
				ps.setString(4, description);
				ps.executeUpdate();
		} catch (SQLException | IOException e) {
			throw new DBAccessException("Unable to create support ticket.");
		}
	}

	
	public static void refreshCartPrices(int cartId) throws DBAccessException {
	    String sql = "update cart_item ci "
	               + "join product p on ci.product_id = p.product_id "
	               + "set ci.price = p.price, "
	               + "    ci.item_total = p.price * ci.quantity "
	               + "where ci.cart_id = ?";

	    try (Connection con = DBUtil.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setInt(1, cartId);
	        ps.executeUpdate();
	    } catch (SQLException | IOException e) {
	        throw new DBAccessException("Unable to refresh cart prices.");
	    }
	}
	
	
	

	public static void cancelOrder(int orderId, int userId) throws DBAccessException, EntityNotFoundException {

	    String checkSql = "select status from `order` where order_id = ? and user_id = ?";
	    String cancelOrderSql = "update `order` set status = 'cancelled' where order_id = ?";
	    String refundPaymentSql = "update payment set payment_status = 'refunded' where order_id = ?";
	    String restoreStockSql = "update inventory set stock_quantity = stock_quantity + ? where product_id = ?";
	    String getItemsSql = "select product_id, quantity from order_item where order_id = ?";

	    try (Connection con = DBUtil.getConnection()) {
	        con.setAutoCommit(false);

	        String currentStatus;
	        try (PreparedStatement checkPs = con.prepareStatement(checkSql)) {
	            checkPs.setInt(1, orderId);
	            checkPs.setInt(2, userId);
	            ResultSet rs = checkPs.executeQuery();
	            if (!rs.next()) {
	                con.rollback();
	                throw new EntityNotFoundException("Order not found or does not belong to you.");
	            }
	            currentStatus = rs.getString("status");
	        }

	        if (!currentStatus.equalsIgnoreCase("placed") && !currentStatus.equalsIgnoreCase("confirmed")) {
	            con.rollback();
	            throw new DBAccessException("Order cannot be cancelled. Current status: " + currentStatus
	                + ". Only 'placed' or 'confirmed' orders can be cancelled.");
	        }

	        try (PreparedStatement itemsPs = con.prepareStatement(getItemsSql)) {
	            itemsPs.setInt(1, orderId);
	            ResultSet itemsRs = itemsPs.executeQuery();

	            try (PreparedStatement restorePs = con.prepareStatement(restoreStockSql)) {
	                while (itemsRs.next()) {
	                    restorePs.setInt(1, itemsRs.getInt("quantity"));
	                    restorePs.setInt(2, itemsRs.getInt("product_id"));
	                    restorePs.executeUpdate();
	                }
	            }
	        }

	        try (PreparedStatement cancelPs = con.prepareStatement(cancelOrderSql)) {
	            cancelPs.setInt(1, orderId);
	            cancelPs.executeUpdate();
	        }

	        try (PreparedStatement refundPs = con.prepareStatement(refundPaymentSql)) {
	            refundPs.setInt(1, orderId);
	            refundPs.executeUpdate();
	        }

	        con.commit();

	    } catch (SQLException | IOException e) {
	        throw new DBAccessException("Failed to cancel order. Please try again.");
	    }
	}
}
