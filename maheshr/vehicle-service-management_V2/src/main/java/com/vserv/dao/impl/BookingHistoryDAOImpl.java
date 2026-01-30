package com.vserv.dao.impl;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.vserv.config.DBConn;
import com.vserv.dao.interfaces.BookingHistoryDAO;
import com.vserv.model.BookingHistory;

public class BookingHistoryDAOImpl implements BookingHistoryDAO {

    @Override
    public BookingHistory insert(BookingHistory history) throws SQLException {
        String sql = """
            INSERT INTO booking_history 
            (booking_id, action_type, old_service_date, new_service_date, 
             old_time_slot, new_time_slot, reason, action_by)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, history.getBookingId());
            stmt.setString(2, history.getActionType());
            
            if (history.getOldServiceDate() != null) {
                stmt.setDate(3, Date.valueOf(history.getOldServiceDate()));
            } else {
                stmt.setNull(3, Types.DATE);
            }
            
            if (history.getNewServiceDate() != null) {
                stmt.setDate(4, Date.valueOf(history.getNewServiceDate()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            
            stmt.setString(5, history.getOldTimeSlot());
            stmt.setString(6, history.getNewTimeSlot());
            stmt.setString(7, history.getReason());
            stmt.setInt(8, history.getActionBy());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                history.setHistoryId(rs.getInt(1));
            }
        }
        return history;
    }

    @Override
    public List<BookingHistory> findByBookingId(int bookingId) throws SQLException {
        List<BookingHistory> histories = new ArrayList<>();
        String sql = """
            select bh.*, u.full_name as action_by_name
            from booking_history bh
            join user u ON bh.action_by = u.user_id
            where bh.booking_id = ?
            ORDER BY bh.action_date DESC
            """;

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                histories.add(mapResultSet(rs));
            }
        }
        return histories;
    }

    @Override
    public List<BookingHistory> findByActionType(String actionType) throws SQLException {
        List<BookingHistory> histories = new ArrayList<>();
        String sql = """
            select bh.*, u.full_name as action_by_name
            from booking_history bh
            join user u ON bh.action_by = u.user_id
            where bh.action_type = ?
            ORDER BY bh.action_date DESC
            """;

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, actionType);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                histories.add(mapResultSet(rs));
            }
        }
        return histories;
    }

    @Override
    public List<BookingHistory> findByUser(int userId) throws SQLException {
        List<BookingHistory> histories = new ArrayList<>();
        String sql = """
            select bh.*, u.full_name as action_by_name
            from booking_history bh
            join user u ON bh.action_by = u.user_id
            where bh.action_by = ?
            ORDER BY bh.action_date DESC
            """;

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                histories.add(mapResultSet(rs));
            }
        }
        return histories;
    }

    @Override
    public void logCreated(int bookingId, int actionBy, LocalDate serviceDate, 
                          String timeSlot) throws SQLException {
        BookingHistory history = new BookingHistory();
        history.setBookingId(bookingId);
        history.setActionType("CREATED");
        history.setNewServiceDate(serviceDate);
        history.setNewTimeSlot(timeSlot);
        history.setActionBy(actionBy);
        insert(history);
    }

    @Override
    public void logRescheduled(int bookingId, int actionBy, 
                              LocalDate oldDate, String oldSlot,
                              LocalDate newDate, String newSlot,
                              String reason) throws SQLException {
        BookingHistory history = new BookingHistory();
        history.setBookingId(bookingId);
        history.setActionType("RESCHEDULED");
        history.setOldServiceDate(oldDate);
        history.setOldTimeSlot(oldSlot);
        history.setNewServiceDate(newDate);
        history.setNewTimeSlot(newSlot);
        history.setReason(reason);
        history.setActionBy(actionBy);
        insert(history);
    }

    @Override
    public void logCancelled(int bookingId, int actionBy, String reason) throws SQLException {
        BookingHistory history = new BookingHistory();
        history.setBookingId(bookingId);
        history.setActionType("CANCELLED");
        history.setReason(reason);
        history.setActionBy(actionBy);
        insert(history);
    }

    @Override
    public void logConfirmed(int bookingId, int actionBy) throws SQLException {
        BookingHistory history = new BookingHistory();
        history.setBookingId(bookingId);
        history.setActionType("CONFIRMED");
        history.setActionBy(actionBy);
        insert(history);
    }

    private BookingHistory mapResultSet(ResultSet rs) throws SQLException {
        BookingHistory history = new BookingHistory();
        history.setHistoryId(rs.getInt("history_id"));
        history.setBookingId(rs.getInt("booking_id"));
        history.setActionType(rs.getString("action_type"));
        
        Date oldDate = rs.getDate("old_service_date");
        if (oldDate != null) {
            history.setOldServiceDate(oldDate.toLocalDate());
        }
        
        Date newDate = rs.getDate("new_service_date");
        if (newDate != null) {
            history.setNewServiceDate(newDate.toLocalDate());
        }
        
        history.setOldTimeSlot(rs.getString("old_time_slot"));
        history.setNewTimeSlot(rs.getString("new_time_slot"));
        history.setReason(rs.getString("reason"));
        history.setActionBy(rs.getInt("action_by"));
        history.setActionByName(rs.getString("action_by_name"));
        
        Timestamp actionDate = rs.getTimestamp("action_date");
        if (actionDate != null) {
            history.setActionDate(actionDate.toLocalDateTime());
        }
        
        return history;
    }
}