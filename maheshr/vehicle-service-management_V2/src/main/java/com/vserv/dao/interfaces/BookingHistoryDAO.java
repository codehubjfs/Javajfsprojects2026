package com.vserv.dao.interfaces;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import com.vserv.model.BookingHistory;

/**
 * DAO interface for booking history operations
 * 
 * @author Mahesh R
 */
public interface BookingHistoryDAO {
    

    BookingHistory insert(BookingHistory history) throws SQLException;
    

    List<BookingHistory> findByBookingId(int bookingId) throws SQLException;
    

    List<BookingHistory> findByActionType(String actionType) throws SQLException;

    List<BookingHistory> findByUser(int userId) throws SQLException;
    

    void logCreated(int bookingId, int actionBy, LocalDate serviceDate, 
                   String timeSlot) throws SQLException;
    void logRescheduled(int bookingId, int actionBy, 
                       LocalDate oldDate, String oldSlot,
                       LocalDate newDate, String newSlot,
                       String reason) throws SQLException;

    void logCancelled(int bookingId, int actionBy, String reason) throws SQLException;
    void logConfirmed(int bookingId, int actionBy) throws SQLException;
}