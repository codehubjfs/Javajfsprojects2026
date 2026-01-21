package dao;

import util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * This class will handle service request data with database.
 * @author KavinT
 * @since 1.0
 */
public class ServiceRequestDAO {

	/**
	 * This method will create new service request and add to database.
	 * @param requestDate
	 * @param status
	 * @param bookingId
	 * @param serviceId
	 * @throws Exception
	 */
    public void createRequest(LocalDateTime requestDate, String status, int bookingId, int serviceId) throws Exception {
        String sql = "INSERT INTO service_request (request_date, request_status, booking_id, service_id) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(requestDate));
            ps.setString(2, status);
            ps.setInt(3, bookingId);
            ps.setInt(4, serviceId);
            ps.executeUpdate();
        }
    }

    /**
     * This method will update the status of service with request_id.
     * @param requestId
     * @param status
     * @throws Exception
     */
    public void updateStatus(int requestId, String status) throws Exception {
        String sql = "UPDATE service_request SET request_status=?, completed_at=NOW() WHERE request_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, requestId);
            ps.executeUpdate();
        }
    }

    /**
     * This method will get all service requests by booking id.
     * @param userId
     * @return
     * @throws Exception
     */
    public ResultSet getServiceRequestsByBookingId(int bookingId) throws Exception {
        String sql =
            "SELECT * FROM service_request" +
            "WHERE booking = ? ORDER BY request_id ASC";

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, bookingId);
        return ps.executeQuery();
    }
    
    /**
     * This will get all service request from database.
     * @return
     * @throws Exception
     */
    public ResultSet getAllServiceRequests() throws Exception {
        String sql = "SELECT sr.request_id, sr.booking_id, sr.request_status, s.service_name " +
                     "FROM service_request sr JOIN service s ON sr.service_id = s.service_id";

        Connection con = DBConnection.getConnection();
        return con.prepareStatement(sql).executeQuery();
    }

}
