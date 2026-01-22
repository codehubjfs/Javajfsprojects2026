package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class MedicineDao {
	
	public static void viewAllMedicines() throws Exception{
		String sql = "select * from medicine";
		try(Connection con = ConnectionPool.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql)){
			System.out.println("medicineId | name | brand | strength | composition |dosageForm |"
					+ "  mrp | prescription | categoryId | storageInstructions | manufactureId | "
					+ "scheduleType | taxPercentage | discountPercentage | finalPrice");
			while(rs.next()) {
				System.out.println(rs.getInt("medicine_id")+"  "+rs.getString("name")+"  "+rs.getString("brand")
				+"  "+rs.getString("strength")+"  "+rs.getString("composition")+"  "+rs.getString("dosage_form")
				+"  "+rs.getBigDecimal("price")+"  "+rs.getInt("prescription_required")+"  "+rs.getInt("category_id")
				+"  "+rs.getString("storage_instructions")+"  "+rs.getInt("manufacturer_id")+"  "+rs.getString("schedule_type")
				+"  "+rs.getInt("tax_percentage")+"  "+rs.getInt("discount_percentage")+" "+rs.getBigDecimal("final_price"));
			}
		}
	}
	
	public static void addMedicine(int medicineId, String name, String brand,String composition, String strength, double mrp, boolean prescription, int categoryId, String storageInstructions,  String dosageForm,
             int manufactureId, String scheduleType, int taxPercentage, int discountPercentage, double finalPrice) throws Exception{
		String addMedicineQuery = "insert into medicine values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		try(Connection conn = ConnectionPool.getConnection();
				PreparedStatement ps = conn.prepareStatement(addMedicineQuery)){
			ps.setInt(1, medicineId);
			ps.setString(2, name);
			ps.setString(3, brand);
			ps.setString(4, composition);
			ps.setString(5, strength);
			ps.setDouble(6, mrp);
			ps.setBoolean(7, prescription);
			ps.setInt(8, categoryId);
			ps.setString(9, storageInstructions);
			ps.setString(10, dosageForm);
			ps.setInt(11, manufactureId);
			ps.setString(12, scheduleType);
			ps.setInt(13, taxPercentage);
			ps.setInt(14, discountPercentage);
			ps.setDouble(15, finalPrice);
			ps.executeUpdate();
			
		}
	}

}
