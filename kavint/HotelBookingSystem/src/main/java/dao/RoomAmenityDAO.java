package dao;

import util.DBConnection;

import java.sql.*;

/**
 * This class will handle Room and Amenity data with database.
 * @author KavinT
 * @since 1.0 
 */
public class RoomAmenityDAO {

	/**
	 * This will assign amenity to room.
	 * @param roomId
	 * @param amenityId
	 * @throws Exception
	 */
    public void assignAmenity(int roomId, int amenityId) throws Exception {
        String sql = "INSERT INTO room_amenity (room_id, amenity_id) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, roomId);
            ps.setInt(2, amenityId);
            ps.executeUpdate();
        }
    }

    /**
     * This method will get all amenity of room by room id.
     * @param roomId
     * @return
     * @throws Exception
     */
    public ResultSet getAmenitiesByRoom(int roomId) throws Exception {
        String sql =
            "SELECT a.amenity_name FROM room_amenity ra " +
            "JOIN amenity a ON ra.amenity_id = a.amenity_id " +
            "WHERE ra.room_id=?";

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, roomId);
        return ps.executeQuery();
    }
}
