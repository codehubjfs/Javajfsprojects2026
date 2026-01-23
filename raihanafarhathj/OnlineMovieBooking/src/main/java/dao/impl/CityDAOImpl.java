package dao.impl;

import dao.CityDAO;
import model.City;
import exception.DataAccessException;
import config.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CityDAOImpl implements CityDAO {

    @Override
    public int addCity(City city) throws DataAccessException {

        String sql = "INSERT INTO city (city_name, state) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, city.getCityName());
            ps.setString(2, city.getState());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // returns generated city_id
                }
            }

            throw new DataAccessException("Failed to generate city ID");

        } catch (SQLException e) {
            throw new DataAccessException("Error adding city", e);
        }
    }

    @Override
    public City getCityById(int cityId) throws DataAccessException {

        String sql = "SELECT * FROM city WHERE city_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cityId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCity(rs);
                }
            }

            return null; // city not found

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching city by ID", e);
        }
    }

    @Override
    public List<City> getAllCities() throws DataAccessException {

        String sql = "SELECT * FROM city ORDER BY city_name";
        List<City> cities = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                cities.add(mapResultSetToCity(rs));
            }

            return cities;

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching all cities", e);
        }
    }

    // ---------- Helper method ----------
    private City mapResultSetToCity(ResultSet rs) throws SQLException {

        City city = new City(rs.getString("city_name"), rs.getString("state"));

        // set auto-increment ID internally
        try {
            java.lang.reflect.Field field = City.class.getDeclaredField("_cityId");
            field.setAccessible(true);
            field.set(city, rs.getInt("city_id"));
        } catch (Exception ignored) {}

        return city;
    }
}
