package dao;

import model.Location;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class will handle the location details with database.
 * @author KavinT
 * @since 1.0
 */
public class LocationDAO {

	/**
	 * This method add location to database.
	 * @param location
	 * @throws Exception
	 */
    public void addLocation(Location location) throws Exception {
        String sql = "INSERT INTO location (address, city, state, country, zipcode, nationality) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, location.getAddress());
            ps.setString(2, location.getCity());
            ps.setString(3, location.getState());
            ps.setString(4, location.getCountry());
            ps.setString(5, location.getZipcode());
            ps.setString(6, location.getNationality());
            ps.executeUpdate();
        }
    }

    /**
     * This method will return location id of tail.
     * @return
     * @throws Exception
     */
    public int getLastInsertedId() throws Exception {
        String sql = "SELECT MAX(location_id) AS lastId FROM location";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("lastId");
            }
            throw new Exception("No Location ID found!");
        }
    }

    /**
     * This will get location detail by location id.
     * @param locationId
     * @return
     * @throws Exception
     */
    public Location getLocationById(int locationId) throws Exception {
        String sql = "SELECT * FROM location WHERE location_id = ?";
        Location location = null;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, locationId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                location = new Location(
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("state"),
                        rs.getString("country"),
                        rs.getString("zipcode"),
                        rs.getString("nationality"),
                        rs.getString("created_at")
                );
            }
        }
        return location;
    }

    /**
     * This will return all location detail.
     * @return
     * @throws Exception
     */
    public List<Location> getAllLocations() throws Exception {
        String sql = "SELECT * FROM location ORDER BY location_id ASC";
        List<Location> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Location location = new Location(
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("state"),
                        rs.getString("country"),
                        rs.getString("zipcode"),
                        rs.getString("nationality"),
                        rs.getString("created_at")
                );
                list.add(location);
            }
        }
        return list;
    }
}
