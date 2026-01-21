package dao;

import util.DBConnection;
import java.sql.*;

/**
 * This class will handle feedback data with database.
 * @author KavinT
 * @since 1.0
 */
public class FeedbackDAO {

	/**
	 * This method will add new feedback to database.
	 * @param bookingId
	 * @param rating
	 * @param comment
	 * @throws Exception
	 */
    public void addFeedback(int bookingId, int rating, String comment) throws Exception {

        String sql = "INSERT INTO feedback VALUES (?, ?, ?, NOW())";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, bookingId);
        ps.setInt(2, rating);
        ps.setString(3, comment);

        ps.executeUpdate();
        con.close();
    }
}
