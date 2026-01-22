package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class InventoryMangementDao {
	
	public static void viewInventory() throws Exception{
		String query = "select * from inventory";
		try(Connection conn = ConnectionPool.getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query)){
			System.out.println("InventoryId | medicineId | batchNumber | epiryDate | quantityAvailable | purchasePrice | sellingPrice");
			while(rs.next()) {
				System.out.println(rs.getInt("inventory_id")+"  "+rs.getInt("medicine_id")+"  "+rs.getInt("batch_number")
				+"  "+rs.getDate("expiry_date")+"  "+rs.getInt("quantity_available")+"  "+rs.getDouble("purchase_price")+"  "+rs.getDouble("selling_price"));
			}
		}
	}
	
	public static void viewLowStockProducts() throws Exception{
		String query = "select * from inventory "
				+ "where quantity_available < 20;";
		
		System.out.println("\n*********Low stock Products*********");
		try(Connection conn = ConnectionPool.getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query)){
			System.out.println("InventoryId | medicineId | batchNumber | epiryDate | quantityAvailable | purchasePrice | sellingPrice");
			while(rs.next()) {
				System.out.println(rs.getInt("inventory_id")+"  "+rs.getInt("medicine_id")+"  "+rs.getInt("batch_number")
				+"  "+rs.getDate("expiry_date")+"  "+rs.getInt("quantity_available")+"  "+rs.getDouble("purchase_price")+"  "+rs.getDouble("selling_price"));
			}
		}
	}
		
		public static void updateInventoryStock(int medicineId, int batchNo) throws Exception{
			String query = "update inventory \r\n"
					+ "set quantity_available = quantity_available + 50\r\n"
					+ "where medicine_id = ? and batch_number = ?;";
			try(Connection conn = ConnectionPool.getConnection();
					PreparedStatement ps = conn.prepareStatement(query)){
				ps.setInt(1,medicineId);
				ps.setInt(2, batchNo);
				ps.executeUpdate();
			}
		}
	

}
