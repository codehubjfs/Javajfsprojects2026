package dao;

import util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * This class will handle payment data with database.
 * @author KavinT
 * @since 1.0
 */
public class PaymentDAO {

	/**
	 * This method will create new payment for user booking.
	 * @param paymentDate
	 * @param method
	 * @param status
	 * @param amount
	 * @param bookingId
	 * @throws Exception
	 */
    public void createPayment(LocalDateTime paymentDate, String method, String status, double amount, int bookingId) throws Exception {
        String sql =
            "INSERT INTO payment (payment_date, payment_method, payment_status, amount, booking_id) " +
            "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(paymentDate));
            ps.setString(2, method);
            ps.setString(3, status);
            ps.setDouble(4, amount);
            ps.setInt(5, bookingId);
            ps.executeUpdate();
        }
    }

    /**
     * This will get payment details by booking id
     * @param userId
     * @return
     * @throws Exception
     */
    public ResultSet getPaymentsByUser(int BookingId) throws Exception {
        String sql =
            "SELECT * FROM payment" +
            "WHERE booking_id = ?";

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, BookingId);
        return ps.executeQuery();
    }
}
