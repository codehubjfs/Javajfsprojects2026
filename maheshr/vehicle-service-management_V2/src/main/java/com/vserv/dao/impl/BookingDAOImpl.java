package com.vserv.dao.impl;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.vserv.config.DBConn;
import com.vserv.dao.interfaces.BookingDAO;
import com.vserv.model.Availability;
import com.vserv.model.Booking;

public class BookingDAOImpl implements BookingDAO {

    @Override
    public List<Availability> findAvailableSlots(LocalDate fromDate) throws SQLException {
        List<Availability> slots = new ArrayList<>();
        String sql = """
                select * FROM service_availability where service_date >= ?
                AND is_available = TRUE AND current_bookings < max_bookings
                ORDER BY service_date, time_slot
                """;

        try (Connection conn = DBConn.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(fromDate));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                slots.add(mapResultSetToAvailability(rs));
            }
        }
        return slots;
    }

    @Override
    public List<Booking> findByUserId(int userId) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = """
                select 
                    sb.booking_id,
                    sb.vehicle_id,
                    sb.catalog_id,
                    sb.service_date,
                    sb.time_slot,
                    sb.booking_status,
                    sb.booking_notes,
                    sb.created_at,
                    v.brand,
                    v.model,
                    v.registration_number,
                    sc.service_name,
                    sr.service_id,
                    sr.status as service_status,
                    sr.remarks as service_remarks,
                    sr.service_start_date,
                    sr.service_end_date,
                    u.full_name as advisor_name
                FROM service_booking sb
                join vehicle v ON sb.vehicle_id = v.vehicle_id
                join service_catalog sc ON sb.catalog_id = sc.catalog_id
                LEFT join service_record sr ON sb.booking_id = sr.booking_id
                LEFT join user u ON sr.advisor_id = u.user_id
                where v.user_id = ?
                ORDER BY sb.service_date DESC, sb.booking_id DESC
                """;

        try (Connection conn = DBConn.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        }
        return bookings;
    }

    @Override
    public List<Booking> findAll() throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = """
                select 
                    sb.booking_id,
                    sb.vehicle_id,
                    sb.catalog_id,
                    sb.service_date,
                    sb.time_slot,
                    sb.booking_status,
                    sb.booking_notes,
                    sb.created_at,
                    v.brand,
                    v.model,
                    v.registration_number,
                    sc.service_name,
                    sr.service_id,
                    sr.status as service_status,
                    sr.remarks as service_remarks,
                    sr.service_start_date,
                    sr.service_end_date,
                    u.full_name as advisor_name
                FROM service_booking sb
                join vehicle v ON sb.vehicle_id = v.vehicle_id
                join service_catalog sc ON sb.catalog_id = sc.catalog_id
                LEFT join service_record sr ON sb.booking_id = sr.booking_id
                LEFT join user u ON sr.advisor_id = u.user_id
                ORDER BY sb.service_date DESC, sb.booking_id DESC
                """;

        try (Connection conn = DBConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        }
        return bookings;
    }

    @Override
    public Booking findById(int bookingId) throws SQLException {
        String sql = """
                select 
                    sb.booking_id,
                    sb.vehicle_id,
                    sb.catalog_id,
                    sb.service_date,
                    sb.time_slot,
                    sb.booking_status,
                    sb.booking_notes,
                    sb.created_at,
                    v.brand,
                    v.model,
                    v.registration_number,
                    sc.service_name,
                    sr.service_id,
                    sr.status as service_status,
                    sr.remarks as service_remarks,
                    sr.service_start_date,
                    sr.service_end_date,
                    u.full_name as advisor_name
                FROM service_booking sb
                join vehicle v ON sb.vehicle_id = v.vehicle_id
                join service_catalog sc ON sb.catalog_id = sc.catalog_id
                LEFT join service_record sr ON sb.booking_id = sr.booking_id
                LEFT join user u ON sr.advisor_id = u.user_id
                where sb.booking_id = ?
                """;

        try (Connection conn = DBConn.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToBooking(rs);
            }
        }
        return null;
    }

    @Override
    public Booking insert(Booking booking) throws SQLException {
        Connection conn = null;
        try {
            conn = DBConn.getConnection();
            conn.setAutoCommit(false);

            String sql = """
                    INSERT INTO service_booking (vehicle_id, catalog_id, service_date,
                        time_slot, booking_status, booking_notes)
                    VALUES (?, ?, ?, ?, ?, ?)
                    """;

            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, booking.getVehicleId());
            stmt.setInt(2, booking.getCatalogId());
            stmt.setDate(3, Date.valueOf(booking.getServiceDate()));
            stmt.setString(4, booking.getTimeSlot());
            stmt.setString(5, booking.getBookingStatus());
            stmt.setString(6, booking.getBookingNotes());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                booking.setBookingId(rs.getInt(1));
            }

            String updateSql = """
                    UPDATE service_availability
                    SET current_bookings = current_bookings + 1
                    where service_date = ?
                      AND time_slot = ?
                    """;
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setDate(1, Date.valueOf(booking.getServiceDate()));
            updateStmt.setString(2, booking.getTimeSlot());
            updateStmt.executeUpdate();

            conn.commit();
            return booking;

        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
        }
    }

    @Override
    public void reschedule(int bookingId, LocalDate newServiceDate, String newTimeSlot, 
                          LocalDate oldServiceDate, String oldTimeSlot) throws SQLException {
        Connection conn = null;

        try {
            conn = DBConn.getConnection();
            conn.setAutoCommit(false);

            String updateBookingSql = "UPDATE service_booking SET service_date = ?, "
                    + "time_slot = ?, booking_status = 'RESCHEDULED' where booking_id = ?";
            PreparedStatement stmt1 = conn.prepareStatement(updateBookingSql);
            stmt1.setDate(1, Date.valueOf(newServiceDate));
            stmt1.setString(2, newTimeSlot);
            stmt1.setInt(3, bookingId);
            stmt1.executeUpdate();

            String oldSlotSql = "UPDATE service_availability SET current_bookings = current_bookings - 1 "
                    + "where service_date = ? AND time_slot = ?";
            PreparedStatement stmt2 = conn.prepareStatement(oldSlotSql);
            stmt2.setDate(1, Date.valueOf(oldServiceDate));
            stmt2.setString(2, oldTimeSlot);
            stmt2.executeUpdate();

            String newSlotSql = "UPDATE service_availability SET current_bookings = current_bookings + 1 "
                    + "where service_date = ? AND time_slot = ?";
            PreparedStatement stmt3 = conn.prepareStatement(newSlotSql);
            stmt3.setDate(1, Date.valueOf(newServiceDate));
            stmt3.setString(2, newTimeSlot);
            stmt3.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
        }
    }

    @Override
    public void cancel(int bookingId, LocalDate serviceDate, String timeSlot) throws SQLException {
        Connection conn = null;

        try {
            conn = DBConn.getConnection();
            conn.setAutoCommit(false);

            String cancSql = "UPDATE service_booking SET booking_status = 'CANCELLED' where booking_id = ?";
            PreparedStatement stmt1 = conn.prepareStatement(cancSql);
            stmt1.setInt(1, bookingId);
            stmt1.executeUpdate();

            String updateSql = "UPDATE service_availability SET current_bookings = current_bookings - 1 "
                    + "where service_date = ? AND time_slot = ?";
            PreparedStatement stmt2 = conn.prepareStatement(updateSql);
            stmt2.setDate(1, Date.valueOf(serviceDate));
            stmt2.setString(2, timeSlot);
            stmt2.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
        }
    }

    @Override
    public int getTotalCount() throws SQLException {
        String sql = "select COUNT(*) FROM service_booking";

        try (Connection conn = DBConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    @Override
    public int getCountByStatus(String... statuses) throws SQLException {
        if (statuses.length == 0) return 0;

        StringBuilder sql = new StringBuilder("select COUNT(*) FROM service_booking where booking_status IN (");
        for (int i = 0; i < statuses.length; i++) {
            sql.append("?");
            if (i < statuses.length - 1) sql.append(",");
        }
        sql.append(")");

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < statuses.length; i++) {
                stmt.setString(i + 1, statuses[i]);
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        
        // Booking fields
        booking.setBookingId(rs.getInt("booking_id"));
        booking.setVehicleId(rs.getInt("vehicle_id"));
        booking.setCatalogId(rs.getInt("catalog_id"));
        booking.setServiceDate(rs.getDate("service_date").toLocalDate());
        booking.setTimeSlot(rs.getString("time_slot"));
        booking.setBookingStatus(rs.getString("booking_status"));
        booking.setBookingNotes(rs.getString("booking_notes"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            booking.setCreatedAt(createdAt.toLocalDateTime());
        }

        // Vehicle info
        String vehicleInfo = String.format("%s %s (%s)", 
            rs.getString("brand"), 
            rs.getString("model"),
            rs.getString("registration_number"));
        booking.setVehicleInfo(vehicleInfo);
        
        // Service info
        booking.setServiceName(rs.getString("service_name"));
        
        // Service Record fields (nullable from LEFT join)
        int serviceId = rs.getInt("service_id");
        if (!rs.wasNull()) {
            booking.setServiceRecordId(serviceId);
            booking.setServiceStatus(rs.getString("service_status"));
            booking.setServiceRemarks(rs.getString("service_remarks"));
            booking.setAdvisorName(rs.getString("advisor_name"));
            
            Timestamp startDate = rs.getTimestamp("service_start_date");
            if (startDate != null) {
                booking.setServiceStartDate(startDate.toLocalDateTime());
            }
            
            Timestamp endDate = rs.getTimestamp("service_end_date");
            if (endDate != null) {
                booking.setServiceEndDate(endDate.toLocalDateTime());
            }
        }

        return booking;
    }
    
    @Override
    public void updateStatus(int bookingId, String status) throws SQLException {
        String sql = "update service_booking SET booking_status = ? where booking_id = ?";
        
        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, bookingId);
            stmt.executeUpdate();
        }
    }

    private Availability mapResultSetToAvailability(ResultSet rs) throws SQLException {
        Availability slot = new Availability();
        slot.setAvailabilityId(rs.getInt("availability_id"));
        slot.setServiceDate(rs.getDate("service_date").toLocalDate());
        slot.setTimeSlot(rs.getString("time_slot"));
        slot.setMaxBookings(rs.getInt("max_bookings"));
        slot.setCurrentBookings(rs.getInt("current_bookings"));
        slot.setAvailable(rs.getBoolean("is_available"));
        return slot;
    }
}