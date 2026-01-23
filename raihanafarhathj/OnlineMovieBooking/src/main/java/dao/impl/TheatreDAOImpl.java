package dao.impl;

import dao.TheatreDAO;
import model.Theatre;
import enums.TheatreStatus;
import exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import config.DBConnection;

public class TheatreDAOImpl implements TheatreDAO {

    @Override
    public int addTheatre(Theatre theatre) throws DataAccessException {
        String sql = """
            INSERT INTO theatre
            (theatre_name, contact_number, status, address_id)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, theatre.getTheatreName());
            ps.setString(2, theatre.getContactNumber());
            ps.setString(3, theatre.getStatus().name());
            ps.setInt(4, theatre.getAddressId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            throw new DataAccessException("Theatre ID generation failed");

        } catch (SQLException e) {
            throw new DataAccessException("Error while adding theatre", e);
        }
    }

    @Override
    public Theatre getTheatreById(int theatreId) throws DataAccessException {
        String sql = "SELECT * FROM theatre WHERE theatre_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, theatreId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapTheatre(rs);
                }
            }
            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching theatre by ID", e);
        }
    }

    @Override
    public List<Theatre> getTheatresByCity(int cityId) throws DataAccessException {
        String sql = """
            SELECT t.*
            FROM theatre t
            JOIN address a ON t.address_id = a.address_id
            WHERE a.city_id = ?
        """;

        List<Theatre> theatres = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cityId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    theatres.add(mapTheatre(rs));
                }
            }
            return theatres;

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching theatres by city", e);
        }
    }

    @Override
    public boolean updateTheatreStatus(int theatreId, String status) throws DataAccessException {
        String sql = "UPDATE theatre SET status = ? WHERE theatre_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, theatreId);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new DataAccessException("Error updating theatre status", e);
        }
    }

    private Theatre mapTheatre(ResultSet rs) throws SQLException {
        Theatre theatre = new Theatre();

        theatre.setTheatreId(rs.getInt("theatre_id"));
        theatre.setTheatreName(rs.getString("theatre_name"));
        theatre.setContactNumber(rs.getString("contact_number"));
        theatre.setStatus(TheatreStatus.valueOf(rs.getString("status")));
        theatre.setAddressId(rs.getInt("address_id"));

        return theatre;
    }
}
