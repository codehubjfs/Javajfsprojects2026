package dao.impl;

import dao.HallDAO;
import model.Hall;
import exception.DataAccessException;
import config.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HallDAOImpl implements HallDAO {

    @Override
    public int addHall(Hall hall) throws DataAccessException {

        String sql = "INSERT INTO hall (hall_name, total_seats, theatre_id) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, hall.getHallName());
            ps.setInt(2, hall.getTotalSeats());
            ps.setInt(3, hall.getTheatreId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // returns generated hall_id
                }
            }

            throw new DataAccessException("Failed to generate hall ID");

        } catch (SQLException e) {
            throw new DataAccessException("Error adding hall", e);
        }
    }

    @Override
    public List<Hall> getHallsByTheatre(int theatreId) throws DataAccessException {

        String sql = "SELECT * FROM hall WHERE theatre_id = ? ORDER BY hall_name";
        List<Hall> halls = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, theatreId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    halls.add(mapResultSetToHall(rs));
                }
            }

            return halls;

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching halls by theatre", e);
        }
    }

    // ---------- Helper method ----------
    private Hall mapResultSetToHall(ResultSet rs) throws SQLException {

        Hall hall = new Hall(
                rs.getString("hall_name"),
                rs.getInt("total_seats"),
                rs.getInt("theatre_id")
        );

        // set auto-increment ID internally
        try {
            java.lang.reflect.Field field = Hall.class.getDeclaredField("_hallId");
            field.setAccessible(true);
            field.set(hall, rs.getInt("hall_id"));
        } catch (Exception ignored) {}

        return hall;
    }
}
