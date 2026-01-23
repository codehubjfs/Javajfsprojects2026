package dao.impl;

import dao.BookingSeatDAO;
import model.BookingSeat;
import exception.DataAccessException;
import config.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingSeatDAOImpl implements BookingSeatDAO {

    @Override
    public int addBookingSeat(BookingSeat bookingSeat) throws DataAccessException {

        String sql = "INSERT INTO booking_seat (booking_id, show_seat_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, bookingSeat.getBookingId());
            ps.setInt(2, bookingSeat.getShowSeatId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    // set auto-generated ID using reflection
                    try {
                        java.lang.reflect.Field field = BookingSeat.class.getDeclaredField("_bookingSeatId");
                        field.setAccessible(true);
                        field.set(bookingSeat, rs.getInt(1));
                    } catch (Exception ignored) {}

                    return rs.getInt(1); // return generated booking_seat_id
                }
            }

            throw new DataAccessException("Failed to generate booking seat ID");

        } catch (SQLException e) {
            throw new DataAccessException("Error adding booking seat", e);
        }
    }

    @Override
    public List<BookingSeat> getSeatsByBooking(int bookingId) throws DataAccessException {

        String sql = "SELECT * FROM booking_seat WHERE booking_id = ?";
        List<BookingSeat> seats = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookingId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BookingSeat seat = new BookingSeat();
                    seat.setBookingId(rs.getInt("booking_id"));
                    seat.setShowSeatId(rs.getInt("show_seat_id"));

                    // set auto-increment ID using reflection
                    try {
                        java.lang.reflect.Field field = BookingSeat.class.getDeclaredField("_bookingSeatId");
                        field.setAccessible(true);
                        field.set(seat, rs.getInt("booking_seat_id"));
                    } catch (Exception ignored) {}

                    seats.add(seat);
                }
            }

            return seats;

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching seats by booking", e);
        }
    }
}
