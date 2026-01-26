package DAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Exceptions.DBAccessException;
import util.DBUtil;

public class UserDAO {
	public static int getUserIdByEmail(String email) throws DBAccessException {

	    String sql = "SELECT user_id FROM user WHERE email=? AND status='active'";

	    try (Connection con = DBUtil.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, email);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            return rs.getInt("user_id");
	        }

	        throw new DBAccessException("User not found or inactive.");

	    } catch (SQLException | IOException e) {
	        throw new DBAccessException("Unable to fetch user.");
	    }
	}

}
