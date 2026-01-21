package dao;

import util.DBConnection;

import java.sql.*;

/**
 * This class will handle the room data with database.
 * @author KavinT
 * @since 1.0
 */
public class RoomDAO {

	/**
	 * This method get all available rooms in database;
	 * @return
	 * @throws Exception
	 */
    public ResultSet getAvailableRooms() throws Exception {
        String sql = "SELECT * FROM room WHERE availability_status='AVAILABLE' ORDER BY room_id ASC";

        Connection con = DBConnection.getConnection();
        return con.prepareStatement(sql).executeQuery();
    }

    /**
     * This method will get all room details.
     * @return
     * @throws Exception
     */
    public ResultSet getAllRooms() throws Exception {
        String sql = "SELECT * FROM room ORDER BY room_id ASC";

        Connection con = DBConnection.getConnection();
        return con.prepareStatement(sql).executeQuery();
    }

    /**
     * This method will add new room to the database.
     * @param number
     * @param type
     * @param maximumGuest
     * @param price
     * @param description
     * @throws Exception
     */
    public void addRoom(String number, String type, int maximumGuest, double price, String description) throws Exception {
        String sql = "INSERT INTO room (room_number, room_type, maximum_guest, "
        		+ "price_per_night, description) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, number);
            ps.setString(2, type);
            ps.setInt(3, maximumGuest);
            ps.setDouble(4, price);
            ps.setString(5, description);
            ps.executeUpdate();
        }
    }

    /**
     * This method will update room available status.
     * @param roomId
     * @param status
     * @throws Exception
     */
    public void updateRoomStatus(int roomId, String status) throws Exception {
        String sql = "UPDATE room SET availability_status=? WHERE room_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, roomId);
            ps.executeUpdate();
        }
    }

    /**
     * This method will get price of room using room id.
     * @param roomId
     * @return
     * @throws Exception
     */
    public double getRoomPrice(int roomId) throws Exception {
        String sql = "SELECT price_per_night FROM room WHERE room_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, roomId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getDouble(1) : 0.0;
        }
    }

    /**
     * This method will delete room.  
     * @param roomId
     * @throws Exception
     */
	public void deleteRoom(int roomId) throws Exception {
		String sql = "UPDATE room SET availability_status= 'MAINTENANCE' WHERE room_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, roomId);
            ps.executeUpdate();
        }
		
	}

	/**
	 * This method will filter room by price.
	 * @param min
	 * @param max
	 * @return
	 * @throws Exception
	 */
	public ResultSet filterByPrice(double min, double max) throws Exception {
		 String sql = "SELECT * FROM room WHERE availability_status='AVAILABLE'AND \r\n"
		 		+ "price_per_night BETWEEN ? AND ? ORDER BY room_id ASC;";

		 try (Connection con = DBConnection.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {

	            ps.setDouble(1, min);
	            ps.setDouble(2, max);
	            return ps.executeQuery();
		 }
		
	}
}
