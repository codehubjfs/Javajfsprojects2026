package com.vserv.service;

import java.sql.SQLException;
import java.util.List;

import com.vserv.dao.impl.NotificationDAOImpl;
import com.vserv.dao.interfaces.NotificationDAO;
import com.vserv.exception.BusinessLogicException;
import com.vserv.model.Notification;

public class NotificationService {
    private NotificationDAO notificationDAO;

    public NotificationService() {
        this.notificationDAO = new NotificationDAOImpl();
    }

    /**
     * Send booking confirmation notification
     */
    public void sendBookingConfirmation(int userId, int bookingId, String vehicleInfo, 
            String serviceName, String serviceDate, String timeSlot) throws BusinessLogicException {
        try {
            Notification notification = new Notification();
            notification.setUserId(userId);
            notification.setNotificationType("BOOKING_CONFIRMATION");
            notification.setTitle("Booking Confirmed");
            notification.setMessage(String.format(
                "Your service booking has been confirmed.\n" +
                "Vehicle: %s\n" +
                "Service: %s\n" +
                "Date: %s at %s",
                vehicleInfo, serviceName, serviceDate, timeSlot
            ));
            notification.setRelatedBookingId(bookingId);

            notificationDAO.insert(notification);
        } catch (SQLException e) {
            throw new BusinessLogicException("Error sending notification: " + e.getMessage());
        }
    }

    /**
     * Send service reminder notification
     */
    public void sendServiceReminder(int userId, int bookingId, String vehicleInfo, 
            String serviceDate) throws BusinessLogicException {
        try {
            Notification notification = new Notification();
            notification.setUserId(userId);
            notification.setNotificationType("SERVICE_REMINDER");
            notification.setTitle("Service Reminder");
            notification.setMessage(String.format(
                "Reminder: Your vehicle service is scheduled for %s\n" +
                "Vehicle: %s\n" +
                "Please ensure your vehicle is ready.",
                serviceDate, vehicleInfo
            ));
            notification.setRelatedBookingId(bookingId);

            notificationDAO.insert(notification);
        } catch (SQLException e) {
            throw new BusinessLogicException("Error sending notification: " + e.getMessage());
        }
    }

    /**
     * Send status update notification
     */
    public void sendStatusUpdate(int userId, int bookingId, String vehicleInfo, 
            String newStatus) throws BusinessLogicException {
        try {
            Notification notification = new Notification();
            notification.setUserId(userId);
            notification.setNotificationType("STATUS_UPDATE");
            notification.setTitle("Service Status Update");
            notification.setMessage(String.format(
                "Your service status has been updated to: %s\n" +
                "Vehicle: %s",
                newStatus, vehicleInfo
            ));
            notification.setRelatedBookingId(bookingId);

            notificationDAO.insert(notification);
        } catch (SQLException e) {
            throw new BusinessLogicException("Error sending notification: " + e.getMessage());
        }
    }

    /**
     * Send service completion notification
     */
    public void sendServiceCompletion(int userId, int bookingId, String vehicleInfo) 
            throws BusinessLogicException {
        try {
            Notification notification = new Notification();
            notification.setUserId(userId);
            notification.setNotificationType("COMPLETION");
            notification.setTitle("Service Completed");
            notification.setMessage(String.format(
                "Great news! Your vehicle service has been completed.\n" +
                "Vehicle: %s\n" +
                "Please proceed to payment and pickup.",
                vehicleInfo
            ));
            notification.setRelatedBookingId(bookingId);

            notificationDAO.insert(notification);
        } catch (SQLException e) {
            throw new BusinessLogicException("Error sending notification: " + e.getMessage());
        }
    }

    /**
     * Send payment reminder notification
     */
    public void sendPaymentReminder(int userId, int bookingId, String vehicleInfo, 
            String amount) throws BusinessLogicException {
        try {
            Notification notification = new Notification();
            notification.setUserId(userId);
            notification.setNotificationType("PAYMENT_REMINDER");
            notification.setTitle("Payment Pending");
            notification.setMessage(String.format(
                "Your invoice is ready for payment.\n" +
                "Vehicle: %s\n" +
                "Amount: ₹%s\n" +
                "Please complete the payment to proceed.",
                vehicleInfo, amount
            ));
            notification.setRelatedBookingId(bookingId);

            notificationDAO.insert(notification);
        } catch (SQLException e) {
            throw new BusinessLogicException("Error sending notification: " + e.getMessage());
        }
    }

    /**
     * Get all notifications for user
     */
    public List<Notification> getUserNotifications(int userId) throws BusinessLogicException {
        try {
            return notificationDAO.findByUserId(userId);
        } catch (SQLException e) {
            throw new BusinessLogicException("Error fetching notifications: " + e.getMessage());
        }
    }

    /**
     * Get unread notifications for user
     */
    public List<Notification> getUnreadNotifications(int userId) throws BusinessLogicException {
        try {
            return notificationDAO.findUnreadByUserId(userId);
        } catch (SQLException e) {
            throw new BusinessLogicException("Error fetching unread notifications: " + e.getMessage());
        }
    }

    /**
     * Mark notification as read
     */
    public void markAsRead(int notificationId) throws BusinessLogicException {
        try {
            notificationDAO.markAsRead(notificationId);
        } catch (SQLException e) {
            throw new BusinessLogicException("Error marking notification as read: " + e.getMessage());
        }
    }

    /**
     * Mark all notifications as read for user
     */
    public void markAllAsRead(int userId) throws BusinessLogicException {
        try {
            notificationDAO.markAllAsRead(userId);
        } catch (SQLException e) {
            throw new BusinessLogicException("Error marking notifications as read: " + e.getMessage());
        }
    }

    /**
     * Get unread count for user
     */
    public int getUnreadCount(int userId) throws BusinessLogicException {
        try {
            return notificationDAO.getUnreadCount(userId);
        } catch (SQLException e) {
            throw new BusinessLogicException("Error getting unread count: " + e.getMessage());
        }
    }
}