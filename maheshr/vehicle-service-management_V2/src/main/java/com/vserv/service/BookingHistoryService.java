package com.vserv.service;

import java.sql.SQLException;
import java.util.List;

import com.vserv.dao.impl.BookingDAOImpl;
import com.vserv.dao.impl.BookingHistoryDAOImpl;
import com.vserv.dao.impl.VehicleDAOImpl;
import com.vserv.dao.interfaces.BookingDAO;
import com.vserv.dao.interfaces.BookingHistoryDAO;
import com.vserv.dao.interfaces.VehicleDAO;
import com.vserv.exception.BusinessLogicException;
import com.vserv.model.Booking;
import com.vserv.model.BookingHistory;
import com.vserv.model.Vehicle;

/**
 * Bookinghistory service
 * 
 * @author Mahesh R
 */
public class BookingHistoryService {
    private BookingHistoryDAO historyDAO;
    private BookingDAO bookingDAO;
    private VehicleDAO vehicleDAO;

    public BookingHistoryService() {
        this.historyDAO = new BookingHistoryDAOImpl();
        this.bookingDAO = new BookingDAOImpl();
        this.vehicleDAO = new VehicleDAOImpl();
    }

    public List<BookingHistory> getBookingHistory(int bookingId, int requestingUserId, 
                                                   String userRole) throws BusinessLogicException {
        try {
            // Admin can view any booking history
            if ("ADMIN".equals(userRole)) {
                return historyDAO.findByBookingId(bookingId);
            }

            // Customer/Advisor verify ownership
            Booking booking = bookingDAO.findById(bookingId);
            if (booking == null) {
                throw new BusinessLogicException("Booking not found");
            }

            Vehicle vehicle = vehicleDAO.findById(booking.getVehicleId());
            
            // Customer must own the vehicle
            if ("CUSTOMER".equals(userRole)) {
                if (vehicle.getUserId() != requestingUserId) {
                    throw new BusinessLogicException("Unauthorized: You can only view history of your own bookings");
                }
            }

            return historyDAO.findByBookingId(bookingId);

        } catch (SQLException e) {
            throw new BusinessLogicException("Error fetching booking history: " + e.getMessage());
        }
    }

    public List<BookingHistory> getUserBookingHistory(int userId) throws BusinessLogicException {
        try {
            return historyDAO.findByUser(userId);
        } catch (SQLException e) {
            throw new BusinessLogicException("Error fetching user history: " + e.getMessage());
        }
    }

    public List<BookingHistory> getHistoryByActionType(String actionType, String userRole) 
            throws BusinessLogicException {
        if (!"ADMIN".equals(userRole)) {
            throw new BusinessLogicException("Unauthorized: Only admins can view history by action type");
        }
        
        try {
            return historyDAO.findByActionType(actionType);
        } catch (SQLException e) {
            throw new BusinessLogicException("Error fetching history: " + e.getMessage());
        }
    }
}