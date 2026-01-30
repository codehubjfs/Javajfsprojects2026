package com.vserv.dao.interfaces;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.vserv.model.Availability;
import com.vserv.model.Booking;

public interface BookingDAO {
    List<Availability> findAvailableSlots(LocalDate fromDate) throws SQLException;
    List<Booking> findByUserId(int userId) throws SQLException;
    List<Booking> findAll() throws SQLException;
    Booking findById(int bookingId) throws SQLException;
    Booking insert(Booking booking) throws SQLException;
    void reschedule(int bookingId, LocalDate newServiceDate, String newTimeSlot, 
                   LocalDate oldServiceDate, String oldTimeSlot) throws SQLException;
    void cancel(int bookingId, LocalDate serviceDate, String timeSlot) throws SQLException;
    int getTotalCount() throws SQLException;
    int getCountByStatus(String... statuses) throws SQLException;
    void updateStatus(int bookingId, String status) throws SQLException;
    
}