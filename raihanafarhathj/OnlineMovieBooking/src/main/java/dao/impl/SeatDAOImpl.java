package dao.impl;

import dao.SeatDAO;
import model.Seat;
import exception.DataAccessException;
import config.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import enums.SeatType;

public class SeatDAOImpl implements SeatDAO {

    @Override
    public int addSeat(Seat seat) throws DataAccessException {

        String sql = """
            INSERT INTO seat (seat_number, seat_type, hall_id)
            VALUES (?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, seat.getSeatNumber());
            ps.setString(2, seat.getSeatType().name());
            ps.setInt(3, seat.getHallId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);  // returns generated seat_id
                }
            }

            throw new DataAccessException("Failed to generate seat_id");

        } catch (SQLException e) {
            throw new DataAccessException("Error adding seat", e);
        }
    }

    @Override
    public List<Seat> getSeatsByHall(int hallId) throws DataAccessException {

        String sql = """
            SELECT * FROM seat
            WHERE hall_id = ?
            ORDER BY seat_number
        """;

        List<Seat> seats = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, hallId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    seats.add(mapResultSetToSeat(rs));
                }
            }

            return seats;

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching seats for hall", e);
        }
    }

    // ---------- Helper method ----------
    private Seat mapResultSetToSeat(ResultSet rs) throws SQLException {
        return new Seat(
                rs.getString("seat_number"),
                SeatType.valueOf(rs.getString("seat_type")),
                rs.getInt("hall_id")
        );
    }
}
