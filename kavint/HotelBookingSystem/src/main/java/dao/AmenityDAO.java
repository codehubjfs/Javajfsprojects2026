package dao;

import util.DBConnection;
import java.sql.*;

/**
 * This class will handle the Amenity data with Database.
 * @author KavinT
 * @since 1.0
 */
public class AmenityDAO {

	/**
	 * This method will add new amenity. 
	 * @param amenityName
	 * @throws Exception
	 */
    public void addAmenity(String amenityName) throws Exception {
        String sql = "INSERT INTO amenity (amenity_name) VALUES (?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, amenityName);
            ps.executeUpdate();
        }
    }

    /**
     * This method will display all amenity.
     * @return
     * @throws Exception
     */
    public ResultSet getAllAmenities() throws Exception {
        String sql = "SELECT * FROM amenity ORDER BY amenity_id ASC";

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        return ps.executeQuery();
    }

    /**
     * This will check whether the amenity is exist or not.
     * @param name
     * @return
     * @throws Exception
     */
    public boolean amenityExists(String name) throws Exception {
        String sql = "SELECT 1 FROM amenity WHERE amenity_name = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }
}
