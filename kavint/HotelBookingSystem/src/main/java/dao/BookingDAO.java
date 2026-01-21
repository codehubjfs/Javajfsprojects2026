package dao;

import util.DBConnection;

import java.sql.*;
import java.time.LocalDate;

/**
 * This class will handle the booking data with database.
 * @author KavinT
 * @since 1.0
 */
public class BookingDAO {

	/**
	 * This will create new booking.
	 * @param userId
	 * @param roomId
	 * @param checkIn
	 * @param checkOut
	 * @param totalAmount
	 * @return
	 * @throws Exception
	 */
    public int createBooking(int userId, int roomId,
                              LocalDate checkIn, LocalDate checkOut,
                              double totalAmount) throws Exception {

        String sql = "INSERT INTO booking (check_in_date, check_out_date, total_amount, user_id, room_id, booked_at) " +
                     "VALUES (?, ?, ?, ?, ?, NOW())";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1, Date.valueOf(checkIn));
            ps.setDate(2, Date.valueOf(checkOut));
            ps.setDouble(3, totalAmount);
            ps.setInt(4, userId);
            ps.setInt(5, roomId);

            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

            throw new Exception("User ID not generated!");
        }
    }

    /**
     * This method will return booking details of pending payments.
     * @param userId
     * @return
     * @throws Exception
     */
    public ResultSet getPendingPayments(int userId) throws Exception {
        String sql = "SELECT * FROM booking WHERE user_id=? AND booking_status='CHECKED_OUT'";

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, userId);

        return ps.executeQuery();
    }
    
    /**
     * This method will return total amount of booking.
     * @param bookingId
     * @return
     * @throws Exception
     */
    public double getBookingAmount(int bookingId) throws Exception {
        String sql = "SELECT total_amount FROM booking WHERE booking_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();

            return rs.next() ? rs.getDouble("total_amount") : 0.0;
        }
    }

    /**
     * This method will return active booking data.
     * @param userId
     * @return
     * @throws Exception
     */
    public ResultSet getActiveBookings(int userId) throws Exception {
        String sql = "SELECT * FROM booking WHERE user_id=? AND booking_status IN ('CONFIRMED','CHECKED_IN')";

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, userId);

        return ps.executeQuery();
    }

    /**
     * This method will return completed booking data.
     * @param userId
     * @return
     * @throws Exception
     */
    public ResultSet getCompletedBookings(int userId) throws Exception {
        String sql = "SELECT * FROM booking WHERE user_id=? AND booking_status='CHECKED_OUT'";

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, userId);

        return ps.executeQuery();
    }
    
    /**
     * This method will update check-in time.
     * @param bookingId
     * @throws Exception
     */
    public void checkIn(int bookingId) throws Exception {
    	String sql = "UPDATE booking SET check_in_time = NOW(),"
    			+ " booking_status = 'CHECKED_IN' "
    			+ "WHERE booking_id = ?" ;
    	Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, bookingId);
        ps.executeUpdate();
    }
    
    /**
     * This method will update check-out time.
     * @param bookingId
     * @throws Exception
     */
    public void checkOut(int bookingId) throws Exception {
    	String sql = "UPDATE booking\r\n"
    			+ "SET booking_status = 'CHECKED_OUT',\r\n"
    			+ "    check_out_time = NOW()\r\n"
    			+ "WHERE booking_id = ?;" ;
    	Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, bookingId);
        ps.executeUpdate();
    }

    /**
     * This method will return booking detail by userId.
     * @param userId
     * @return
     * @throws Exception
     */
	public ResultSet getBookingsByUser(int userId) throws Exception {
		String sql = "SELECT * FROM booking\r\n"
				+ "WHERE user_id = ? and booking_status = 'CONFIRMED'";
		Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, userId);

        return ps.executeQuery();
	}

	/**
	 * This method will cancel booking.
	 * @param bookingId
	 * @param roomId
	 * @throws Exception
	 */
	public void cancelBooking(int bookingId, int roomId) throws Exception {
		String sql = "call UpdateBookingCancellation(?, ?)" ;
    	Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, bookingId);
        ps.setInt(2, roomId);
        ps.executeUpdate();
		
	}
	
	/**
     * This method will return booking detail by booking id.
     * @param userId
     * @return
     * @throws Exception
     */
	public ResultSet getBookingsBybooking(int bookingId) throws Exception {
		String sql = "SELECT * FROM booking\r\n"
				+ "WHERE booking_id = ? and booking_status = 'CONFIRMED'";
		Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, bookingId);

        return ps.executeQuery();
	}
	
	/**
	 * This method will return all booking details.
	 * @return
	 * @throws Exception
	 */
	public ResultSet getBookings() throws Exception {
		String sql = "SELECT * FROM booking\r\n"
				+ "WHERE booking_status IN ('CONFIRMED','CHECKED_IN')";
		Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        return ps.executeQuery();
	}
	
	public ResultSet getRoomIdByBookingId(int bookingId) throws Exception {
		String sql = "SELECT room_id FROM booking\r\n"
				+ "WHERE booking_id = ?";
		Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, bookingId);

        return ps.executeQuery();
	}
}
