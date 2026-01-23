package dao.impl;

import dao.ShowDetailsDAO;
import model.ShowDetails;
import exception.DataAccessException;
import config.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import enums.ShowStatus;

public class ShowDetailsDAOImpl implements ShowDetailsDAO {

    @Override
    public int addShow(ShowDetails show) throws DataAccessException {
        String sql = """
            INSERT INTO show_details 
            (show_date, start_time, end_time, ticket_price, show_status, movie_id, hall_id)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1, Date.valueOf(show.getShowDate()));
            ps.setTime(2, Time.valueOf(show.getStartTime()));
            ps.setTime(3, Time.valueOf(show.getEndTime()));
            ps.setDouble(4, show.getTicketPrice());
            ps.setString(5, show.getShowStatus().name());
            ps.setInt(6, show.getMovieId());
            ps.setInt(7, show.getHallId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            throw new DataAccessException("Failed to generate show ID");

        } catch (SQLException e) {
            throw new DataAccessException("Error adding show", e);
        }
    }

    @Override
    public ShowDetails getShowById(int showId) throws DataAccessException {
        String sql = "SELECT * FROM show_details WHERE show_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, showId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToShow(rs);
                }
            }
            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching show by ID", e);
        }
    }

    @Override
    public List<ShowDetails> getShowsByMovie(int movieId, LocalDate date)
            throws DataAccessException {

        String sql = """
            SELECT * FROM show_details
            WHERE movie_id = ? AND show_date = ?
            ORDER BY start_time
        """;

        List<ShowDetails> shows = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, movieId);
            ps.setDate(2, Date.valueOf(date));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    shows.add(mapResultSetToShow(rs));
                }
            }

            return shows;

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching shows by movie", e);
        }
    }

    @Override
    public List<ShowDetails> getShowsByHall(int hallId, LocalDate date)
            throws DataAccessException {

        String sql = """
            SELECT * FROM show_details
            WHERE hall_id = ? AND show_date = ?
            ORDER BY start_time
        """;

        List<ShowDetails> shows = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, hallId);
            ps.setDate(2, Date.valueOf(date));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    shows.add(mapResultSetToShow(rs));
                }
            }

            return shows;

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching shows by hall", e);
        }
    }

    @Override
    public boolean updateShowStatus(int showId, String status)
            throws DataAccessException {

        String sql = "UPDATE show_details SET show_status = ? WHERE show_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, showId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Error updating show status", e);
        }
    }

    @Override
    public List<ShowDetails> getActiveShowsByMovie(int movieId, LocalDate date)
            throws DataAccessException {

        String sql = """
            SELECT * FROM show_details
            WHERE movie_id = ? 
              AND show_date = ?
              AND show_status = 'SCHEDULED'
            ORDER BY start_time
        """;

        List<ShowDetails> shows = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, movieId);
            ps.setDate(2, Date.valueOf(date));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    shows.add(mapResultSetToShow(rs));
                }
            }

            return shows;

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching active shows", e);
        }
    }

    // ---------- Helper method ----------
    private ShowDetails mapResultSetToShow(ResultSet rs) throws SQLException {

        return new ShowDetails(
                rs.getDate("show_date").toLocalDate(),
                rs.getTime("start_time").toLocalTime(),
                rs.getTime("end_time").toLocalTime(),
                rs.getDouble("ticket_price"),
                ShowStatus.valueOf(rs.getString("show_status")),
                rs.getInt("movie_id"),
                rs.getInt("hall_id")
        );
    }
}
