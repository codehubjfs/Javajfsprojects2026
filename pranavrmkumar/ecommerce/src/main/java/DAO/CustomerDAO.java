package DAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Exceptions.DBAccessException;
import Model.CartItem;
import Model.Order;
import Model.Product;
import util.DBUtil;

public class CustomerDAO {

//View products
	public static ArrayList<Product> getActiveProducts() throws DBAccessException {
        ArrayList<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM product WHERE status = 'active'";

        try (Connection con = DBUtil.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("product_id"),
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getString("image_url"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException | IOException e) {
            throw new DBAccessException("Unable to fetch products.");
        }

        return products;
	}
	
	
//cart for the customer
	// cart for the customer
	public static int getOrCreateCart(String email) throws DBAccessException {

	    int userId = UserDAO.getUserIdByEmail(email);

	    String checkSql = "SELECT cart_id FROM cart WHERE user_id=?";

	    try (Connection con = DBUtil.getConnection();
	         PreparedStatement ps = con.prepareStatement(checkSql)) {

	        ps.setInt(1, userId);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            return rs.getInt("cart_id");
	        }

	        String insertSql = "INSERT INTO cart(user_id,total_amount) VALUES(?,0)";
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
	    return -1;
	}



	
//Add to cart
	public static void addOrUpdateCartItem(int cartId, Product product, int quantity)
	        throws DBAccessException {

	    String checkSql =
	            "SELECT quantity FROM cart_item WHERE cart_id=? AND product_id=?";

	    try (Connection con = DBUtil.getConnection();
	         PreparedStatement checkPs = con.prepareStatement(checkSql)) {

	        checkPs.setInt(1, cartId);
	        checkPs.setInt(2, product.getProductID());
	        ResultSet rs = checkPs.executeQuery();

	        if (rs.next()) {
	            String updateSql =
	                "UPDATE cart_item SET quantity = quantity + ?, " +
	                "item_total = item_total + (? * ?) " +
	                "WHERE cart_id=? AND product_id=?";

	            try (PreparedStatement ps = con.prepareStatement(updateSql)) {
	                ps.setInt(1, quantity);
	                ps.setDouble(2, product.getPrice());
	                ps.setInt(3, quantity);
	                ps.setInt(4, cartId);
	                ps.setInt(5, product.getProductID());
	                ps.executeUpdate();
	            }

	        } else {
	            String insertSql =
	                "INSERT INTO cart_item(cart_id, product_id, quantity, price, item_total) " +
	                "VALUES (?,?,?,?,?)";

	            try (PreparedStatement ps = con.prepareStatement(insertSql)) {
	                ps.setInt(1, cartId);
	                ps.setInt(2, product.getProductID());
	                ps.setInt(3, quantity);
	                ps.setDouble(4, product.getPrice());
	                ps.setDouble(5, product.getPrice() * quantity);
	                ps.executeUpdate();
	            }
	        }

	    } catch (SQLException | IOException e) {
	        throw new DBAccessException("Unable to add item to cart.");
	    }
	}

	
	
	
	public static Product getProductById(int productId) throws DBAccessException {

	    String sql = "SELECT * FROM product WHERE product_id = ? AND status='active'";

	    try (Connection con = DBUtil.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, productId);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            return new Product(
	                    rs.getInt("product_id"),
	                    rs.getInt("category_id"),
	                    rs.getString("name"),
	                    rs.getString("brand"),
	                    rs.getDouble("price"),
	                    rs.getString("description"),
	                    rs.getString("image_url"),
	                    rs.getString("status")
	            );
	        }
	        return null;

	    } catch (SQLException | IOException e) {
	        throw new DBAccessException("Unable to fetch product.");
	    }
	}


	
	
	
	public static int getStock(int productId) throws DBAccessException {

	    String sql = "SELECT stock_quantity FROM inventory WHERE product_id=?";

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


	
	
	
	public static ArrayList<CartItem> viewCart(int cartId) throws DBAccessException {

	    ArrayList<CartItem> cartItems = new ArrayList<>();

	    String sql = "SELECT p.*, ci.quantity, ci.price, ci.item_total FROM cart_item ci JOIN product p ON ci.product_id = p.product_id WHERE ci.cart_id = ?";

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


	
	
	public static void createOrder(String email,
            ArrayList<CartItem> items,
            double total)
throws DBAccessException {

String orderSql =
"INSERT INTO `order`(user_id, total_amount, status) VALUES (?, ?, 'placed')";

String orderItemSql =
"INSERT INTO order_item(order_id, product_id, quantity, price, item_total) " +
"VALUES (?, ?, ?, ?, ?)";

String stockUpdateSql =
"UPDATE inventory SET stock_quantity = stock_quantity - ? WHERE product_id = ?";

String clearCartSql =
"DELETE FROM cart_item WHERE cart_id = ?";

try (Connection con = DBUtil.getConnection()) {

con.setAutoCommit(false); // 🔥 TRANSACTION START

int userId = UserDAO.getUserIdByEmail(email);
int cartId = CustomerDAO.getOrCreateCart(email);

// 1️⃣ Create Order
PreparedStatement orderPs =
con.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);

orderPs.setInt(1, userId);
orderPs.setDouble(2, total);
orderPs.executeUpdate();

ResultSet rs = orderPs.getGeneratedKeys();
rs.next();
int orderId = rs.getInt(1);

// 2️⃣ Order Items + Inventory Update
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

// 3️⃣ Clear Cart
PreparedStatement clearPs = con.prepareStatement(clearCartSql);
clearPs.setInt(1, cartId);
clearPs.executeUpdate();

con.commit(); // ✅ TRANSACTION SUCCESS

} catch (SQLException | IOException e) {
throw new DBAccessException("Order placement failed.");
}
}
	
	
	
	
	public static List<Order> getOrders(String email)
	        throws DBAccessException, IOException {

	    List<Order> orders = new ArrayList<>();

	    int userId = UserDAO.getUserIdByEmail(email);

	    String sql =
	        "SELECT o.order_id, o.order_date, o.total_amount, " +
	        "p.product_id, p.name, p.price, " +
	        "oi.quantity " +
	        "FROM `order` o " +
	        "JOIN order_item oi ON o.order_id = oi.order_id " +
	        "JOIN product p ON oi.product_id = p.product_id " +
	        "WHERE o.user_id = ? " +
	        "ORDER BY o.order_date DESC";

	    try (Connection con = DBUtil.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, userId);
	        ResultSet rs = ps.executeQuery();

	        Map<Integer, List<CartItem>> orderItemsMap = new HashMap<>();
	        Map<Integer, OrderMeta> orderMetaMap = new HashMap<>();

	        while (rs.next()) {

	            int orderId = rs.getInt("order_id");

	            orderMetaMap.putIfAbsent(orderId,
	                new OrderMeta(
	                    rs.getDouble("total_amount"),
	                    rs.getTimestamp("order_date").toLocalDateTime().toLocalDate()
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
	            OrderMeta meta = orderMetaMap.get(orderId);
	            orders.add(new Order(
	                orderId,
	                orderItemsMap.get(orderId),
	                meta.totalAmount,
	                meta.orderDate
	            ));
	        }

	    } catch (SQLException e) {
	        e.printStackTrace(); // keep this while debugging
	        throw new DBAccessException("Unable to fetch orders.");
	    }

	    return orders;
	}


	/* Helper inner class */
	static class OrderMeta {
	    double totalAmount;
	    LocalDate orderDate;

	    OrderMeta(double totalAmount, LocalDate orderDate) {
	        this.totalAmount = totalAmount;
	        this.orderDate = orderDate;
	    }
	}



}
