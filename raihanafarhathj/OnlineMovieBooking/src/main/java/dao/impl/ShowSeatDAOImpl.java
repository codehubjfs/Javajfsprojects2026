package dao.impl;

import dao.ShowSeatDAO;
import model.ShowSeat;
import exception.DataAccessException;
import config.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import enums.SeatStatus;

public class ShowSeatDAOImpl implements ShowSeatDAO {

    @Override
    public int addShowSeat(ShowSeat showSeat) throws DataAccessException {

        String sql = """
            INSERT INTO show_seat (seat_status, show_id, seat_id)
            VALUES (?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, showSeat.getSeatStatus().name());
            ps.setInt(2, showSeat.getShowId());
            ps.setInt(3, showSeat.getSeatId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            throw new DataAccessException("Failed to generate show_seat_id");

        } catch (SQLException e) {
            throw new DataAccessException("Error adding show seat", e);
        }
    }

    @Override
    public List<ShowSeat> getSeatsByShow(int showId) throws DataAccessException {

        String sql = """
            SELECT * FROM show_seat
            WHERE show_id = ?
            ORDER BY show_seat_id
        """;

        List<ShowSeat> seats = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, showId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    seats.add(mapResultSetToShowSeat(rs));
                }
            }

            return seats;

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching seats for show", e);
        }
    }

    @Override
    public boolean updateSeatStatus(int showSeatId, String status)
            throws DataAccessException {

        String sql = """
            UPDATE show_seat
            SET seat_status = ?
            WHERE show_seat_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, showSeatId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Error updating seat status", e);
        }
    }

    // ---------- Helper method ----------
    private ShowSeat mapResultSetToShowSeat(ResultSet rs) throws SQLException {

        return new ShowSeat(
                SeatStatus.valueOf(rs.getString("seat_status")),
                rs.getInt("show_id"),
                rs.getInt("seat_id")
        );
    }
}
