package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;
import enums.OrderStatus;
import enums.PaymentStatus;

public class OrderDao {
	public static void viewOrders() throws Exception{
		String sql = "select * from orders;";
		try(Connection conn = ConnectionPool.getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(sql)){
			System.out.println("orderId | orderDate | totalAmount | deliveryAddress | orderStatus | paymentStatus | userId");
			while(rs.next()) {
				System.out.println(rs.getInt("order_id")+"  "+rs.getDate("order_date")+" "+rs.getDouble("tot_amount")+" "
						+ " "+rs.getString("delivery_address")+" "+rs.getString("order_status")+" "+rs.getString("payment_status")
						+" "+rs.getInt("user_id"));
			}
		}
	}
	
	public static void updateOrderStatus(int statusNumber,int orderId) throws Exception{
		String query = "update orders\r\n"
				+ "set order_status = '?'\r\n"
				+ "where order_id = ?;";
		
		OrderStatus status = OrderStatus.values()[statusNumber];
		try(Connection conn = ConnectionPool.getConnection();
				PreparedStatement ps = conn.prepareStatement(query)){
			ps.setString(1, status.name());
			ps.setInt(2,orderId);
			ps.executeUpdate();
		}
		
	}
	
	public static void cancelOrder(int orderId) throws Exception{
		String query = "";
		OrderStatus statusOfOrder = OrderStatus.cancel;
		try(Connection conn = ConnectionPool.getConnection();
				PreparedStatement ps = conn.prepareStatement(query)){
			ps.setString(1, statusOfOrder.name());
			ps.executeUpdate();
		}
	}
	
}
