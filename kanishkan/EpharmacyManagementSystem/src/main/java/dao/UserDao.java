package dao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class UserDao {
	
	public static void viewUsers() throws Exception{
		String sqlQuery = "select * from users;";
		try(Connection conn = ConnectionPool.getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(sqlQuery)){
			System.out.println("Id |  Email      |  Phone");
			while(rs.next()) {
				System.out.println(rs.getInt("user_id")+"   "+rs.getString("email")+"  "+ rs.getString("phone"));
			}
			
		}
		
	}
	
	public static void updateUserDetails() throws Exception{
		String updateQuery = "";
		try(Connection conn = ConnectionPool.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateQuery)){
			ps.executeUpdate();
		}
	}
	public static void deactivateUser(int id) throws Exception{
		String deactiveQuery = "update users\r\n"
				+ "set status = \"Inactive\"\r\n"
				+ "where user_id = ?;";
		try(Connection conn = ConnectionPool.getConnection();
				PreparedStatement ps = conn.prepareStatement(deactiveQuery)){
			ps.setInt(1, id);
			ps.executeUpdate();
		}
	}

}
