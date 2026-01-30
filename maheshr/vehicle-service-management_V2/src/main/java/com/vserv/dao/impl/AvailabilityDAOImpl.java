package com.vserv.dao.impl;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.vserv.config.DBConn;
import com.vserv.dao.interfaces.AvailabilityDAO;
import com.vserv.model.Availability;

public class AvailabilityDAOImpl implements AvailabilityDAO {

    @Override
    public List<Availability> findAll() throws SQLException {
        List<Availability> slots = new ArrayList<>();
        String sql = """
                select * from service_availability
                order by service_date, time_slot
                """;

        try (Connection conn = DBConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                slots.add(mapResultSetToAvailability(rs));
            }
        }
        return slots;
    }

    @Override
    public Availability findByDateAndSlot(LocalDate serviceDate, String timeSlot) 
            throws SQLException {
        String sql = """
                select * from service_availability
                where service_date = ? and time_slot = ?
                """;

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(serviceDate));
            stmt.setString(2, timeSlot);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAvailability(rs);
            }
        }
        return null;
    }

    @Override
    public Availability insert(Availability slot) throws SQLException {
        String sql = """
                insert into service_availability (service_date, time_slot, 
                    max_bookings, current_bookings, is_available)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDate(1, Date.valueOf(slot.getServiceDate()));
            stmt.setString(2, slot.getTimeSlot());
            stmt.setInt(3, slot.getMaxBookings());
            stmt.setInt(4, slot.getCurrentBookings());
            stmt.setBoolean(5, slot.isAvailable());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                slot.setAvailabilityId(rs.getInt(1));
            }
        }
        return slot;
    }

    @Override
    public void update(int availabilityId, int maxBookings, boolean isAvailable) 
            throws SQLException {
        String sql = """
                update service_availability 
                SET max_bookings = ?, is_available = ?
                where availability_id = ?
                """;

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maxBookings);
            stmt.setBoolean(2, isAvailable);
            stmt.setInt(3, availabilityId);

            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int availabilityId) throws SQLException {
        String sql = "delete from service_availability where availability_id = ?";

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, availabilityId);
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