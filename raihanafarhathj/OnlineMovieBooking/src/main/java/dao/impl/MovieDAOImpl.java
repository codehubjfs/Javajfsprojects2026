package dao.impl;

import dao.MovieDAO;
import model.Movie;
import exception.DataAccessException;
import config.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import enums.AgeRating;
import enums.MovieStatus;

public class MovieDAOImpl implements MovieDAO {

    @Override
    public int addMovie(Movie movie) throws DataAccessException {

        String sql = """
            INSERT INTO movie (movie_name, language, genre, duration, rating, age_rating, status)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, movie.getMovieName());
            ps.setString(2, movie.getLanguage());
            ps.setString(3, movie.getGenre());
            ps.setInt(4, movie.getDuration());
            ps.setDouble(5, movie.getRating());
            ps.setString(6, movie.getAgeRating().name());
            ps.setString(7, movie.getStatus().name());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);  // returns generated movie_id
                }
            }

            throw new DataAccessException("Failed to generate movie ID");

        } catch (SQLException e) {
            throw new DataAccessException("Error adding movie", e);
        }
    }

    @Override
    public Movie getMovieById(int movieId) throws DataAccessException {

        String sql = "SELECT * FROM movie WHERE movie_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, movieId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMovie(rs);
                } else {
                    return null; // or handle with NotFoundException
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching movie by ID", e);
        }
    }

    @Override
    public List<Movie> getMoviesByStatus(String status) throws DataAccessException {

        String sql = "SELECT * FROM movie WHERE status = ? ORDER BY movie_name";

        List<Movie> movies = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    movies.add(mapResultSetToMovie(rs));
                }
            }

            return movies;

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching movies by status", e);
        }
    }

    // ---------- Helper method ----------
    private Movie mapResultSetToMovie(ResultSet rs) throws SQLException {

        Movie movie = new Movie(
                rs.getString("movie_name"),
                rs.getString("language"),
                rs.getString("genre"),
                rs.getInt("duration"),
                rs.getDouble("rating"),
                AgeRating.valueOf(rs.getString("age_rating")),
                MovieStatus.valueOf(rs.getString("status"))
        );

        // set auto-increment ID internally
        try {
            java.lang.reflect.Field field = Movie.class.getDeclaredField("_movieId");
            field.setAccessible(true);
            field.set(movie, rs.getInt("movie_id"));
        } catch (Exception ignored) {}

        return movie;
    }
}
