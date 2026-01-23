package dao.impl;

import dao.BookingDAO;
import model.Booking;
import exception.DataAccessException;
import config.DBConnection;
import enums.BookingStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImpl implements BookingDAO {

    @Override
    public int addBooking(Booking booking) throws DataAccessException {
        String sql = "INSERT INTO booking (total_amount, booking_status, customer_id, show_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDouble(1, booking.getTotalAmount());
            ps.setString(2, booking.getBookingStatus().name());
            ps.setInt(3, booking.getCustomerId());
            ps.setInt(4, booking.getShowId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);

                    // Set _bookingId using reflection
                    java.lang.reflect.Field fieldId = Booking.class.getDeclaredField("_bookingId");
                    fieldId.setAccessible(true);
                    fieldId.set(booking, generatedId);

                    // Set _bookingTime using reflection to current time
                    java.lang.reflect.Field fieldTime = Booking.class.getDeclaredField("_bookingTime");
                    fieldTime.setAccessible(true);
                    fieldTime.set(booking, java.time.LocalDateTime.now());

                    return generatedId;
                }
            }

            throw new DataAccessException("Failed to generate booking ID");

        } catch (SQLException e) {
            throw new DataAccessException("Error adding booking", e);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new DataAccessException("Reflection error while setting booking ID/time", e);
        }
    }

    @Override
    public Booking getBookingById(int bookingId) throws DataAccessException {
        String sql = "SELECT * FROM booking WHERE booking_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookingId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBooking(rs);
                }
            }

            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching booking by ID", e);
        }
    }

    @Override
    public List<Booking> getBookingsByUser(int userId) throws DataAccessException {
        String sql = "SELECT * FROM booking WHERE customer_id = ? ORDER BY booking_time DESC";
        List<Booking> bookings = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }

            return bookings;

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching bookings by user", e);
        }
    }

    // ---------- Helper method ----------
    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException, DataAccessException {
        Booking booking = new Booking();
        booking.setTotalAmount(rs.getDouble("total_amount"));

        // Correctly convert string from DB to enum
        String statusStr = rs.getString("booking_status");
        booking.setBookingStatus(BookingStatus.valueOf(statusStr.trim().toUpperCase()));

        booking.setCustomerId(rs.getInt("customer_id"));
        booking.setShowId(rs.getInt("show_id"));

        try {
            // Set _bookingId
            java.lang.reflect.Field fieldId = Booking.class.getDeclaredField("_bookingId");
            fieldId.setAccessible(true);
            fieldId.set(booking, rs.getInt("booking_id"));

            // Set _bookingTime
            java.lang.reflect.Field fieldTime = Booking.class.getDeclaredField("_bookingTime");
            fieldTime.setAccessible(true);
            Timestamp ts = rs.getTimestamp("booking_time");
            if (ts != null) {
                fieldTime.set(booking, ts.toLocalDateTime());
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new DataAccessException("Reflection error while mapping booking", e);
        }

        return booking;
    }
}
