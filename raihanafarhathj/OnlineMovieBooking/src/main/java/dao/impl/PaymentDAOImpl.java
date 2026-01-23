package dao.impl;

import dao.PaymentDAO;
import model.Payment;
import exception.DataAccessException;
import config.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;

import enums.PaymentMode;
import enums.PaymentStatus;

public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public int addPayment(Payment payment) throws DataAccessException {

        String sql = """
            INSERT INTO payment (payment_mode, payment_status, payment_time, booking_id)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, payment.getPaymentMode().name());
            ps.setString(2, payment.getPaymentStatus().name());
            ps.setTimestamp(3, Timestamp.valueOf(payment.getPaymentTime() != null 
                                                 ? payment.getPaymentTime() 
                                                 : LocalDateTime.now()));
            ps.setInt(4, payment.getBookingId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);  // returns generated payment_id
                }
            }

            throw new DataAccessException("Failed to generate payment ID");

        } catch (SQLException e) {
            throw new DataAccessException("Error adding payment", e);
        }
    }

    @Override
    public Payment getPaymentByBooking(int bookingId) throws DataAccessException {

        String sql = "SELECT * FROM payment WHERE booking_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookingId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPayment(rs);
                } else {
                    return null; // or handle with NotFoundException at service layer
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching payment for booking", e);
        }
    }

    // ---------- Helper method ----------
    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment(
                PaymentMode.valueOf(rs.getString("payment_mode")),
                PaymentStatus.valueOf(rs.getString("payment_status")),
                rs.getInt("booking_id")
        );

        // set auto-increment ID internally
        try {
            java.lang.reflect.Field field = Payment.class.getDeclaredField("_paymentId");
            field.setAccessible(true);
            field.set(payment, rs.getInt("payment_id"));

            java.lang.reflect.Field timeField = Payment.class.getDeclaredField("_paymentTime");
            timeField.setAccessible(true);
            timeField.set(payment, rs.getTimestamp("payment_time").toLocalDateTime());
        } catch (Exception ignored) {}

        return payment;
    }
}
