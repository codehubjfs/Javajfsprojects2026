package com.vserv.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import com.vserv.dao.impl.*;
import com.vserv.dao.interfaces.*;
import com.vserv.exception.BusinessLogicException;
import com.vserv.model.*;

public class AdvisorService {
    private RecordDAO recordDAO;
    private WorkItemDAO workItemDAO;
    private ItemDAO serviceItemDAO;
    private AdvisorDAO advisorDAO;
    private BookingDAO bookingDAO;
    private NotificationService notificationService;
    private VehicleDAO vehicleDAO;

    public AdvisorService() {
        this.recordDAO = new RecordDAOImpl();
        this.workItemDAO = new WorkItemDAOImpl();
        this.serviceItemDAO = new ItemDAOImpl();
        this.advisorDAO = new AdvisorDAOImpl();
        this.bookingDAO = new BookingDAOImpl();
        this.notificationService = new NotificationService();
        this.vehicleDAO = new VehicleDAOImpl();
    }

    public List<ServiceRecord> getAssignedServices(int advisorId) throws BusinessLogicException {
        try {
            return recordDAO.findByAdvisorId(advisorId);
        } catch (SQLException e) {
            throw new BusinessLogicException("Error fetching assigned services: " + e.getMessage());
        }
    }

    public List<WorkItem> getWorkItems(String carType) throws BusinessLogicException {
        try {
            return workItemDAO.findByCarType(carType);
        } catch (SQLException e) {
            throw new BusinessLogicException("Error fetching work items: " + e.getMessage());
        }
    }

    public List<ServiceItem> getServiceItems(int serviceId) throws BusinessLogicException {
        try {
            return serviceItemDAO.findByServiceId(serviceId);
        } catch (SQLException e) {
            throw new BusinessLogicException("Error fetching service items: " + e.getMessage());
        }
    }

    public ServiceItem addServiceItem(int serviceId, int workItemId, int quantity) 
            throws BusinessLogicException {
        try {
            WorkItem workItem = workItemDAO.findById(workItemId);
            if (workItem == null) {
                throw new BusinessLogicException("Work item not found");
            }

            ServiceItem item = new ServiceItem();
            item.setServiceId(serviceId);
            item.setWorkItemId(workItemId);
            item.setQuantity(quantity);
            item.setUnitPrice(workItem.getUnitPrice());

            return serviceItemDAO.insert(item);
        } catch (SQLException e) {
            throw new BusinessLogicException("Error adding service item: " + e.getMessage());
        }
    }

    public void deleteServiceItem(int itemId) throws BusinessLogicException {
        try {
            serviceItemDAO.delete(itemId);
        } catch (SQLException e) {
            throw new BusinessLogicException("Error deleting service item: " + e.getMessage());
        }
    }

public void startService(int serviceId) throws BusinessLogicException {
    try {
        ServiceRecord record = recordDAO.findById(serviceId);
        if (record == null) {
            throw new BusinessLogicException("Service record not found");
        }

        recordDAO.updateStatus(serviceId, "IN_PROGRESS");

        // Get booking and vehicle info
        Booking booking = bookingDAO.findById(record.getBookingId());
        Vehicle vehicle = vehicleDAO.findById(booking.getVehicleId());
        String vehicleInfo = String.format("%s %s (%s)", 
            vehicle.getBrand(), vehicle.getModel(), vehicle.getRegistrationNumber());

        // Send status update notification
        notificationService.sendStatusUpdate(
            vehicle.getUserId(),
            booking.getBookingId(),
            vehicleInfo,
            "IN PROGRESS"
        );

    } catch (SQLException e) {
        throw new BusinessLogicException("Error starting service: " + e.getMessage());
    }
}

    public void updateRemarks(int serviceId, String remarks) throws BusinessLogicException {
        try {
            recordDAO.updateRemarks(serviceId, remarks);
        } catch (SQLException e) {
            throw new BusinessLogicException("Error updating remarks: " + e.getMessage());
        }
    }

public void completeService(int serviceId, int advisorId, double actualHours) throws BusinessLogicException {
    try {
        ServiceRecord record = recordDAO.findById(serviceId);
        if (record == null) {
            throw new BusinessLogicException("Service record not found");
        }

        if (record.getAdvisorId() != advisorId) {
            throw new BusinessLogicException("Unauthorized: This service is not assigned to you");
        }
        
        // Validate actual hours
        if (actualHours <= 0) {
            throw new BusinessLogicException("Actual hours must be greater than 0");
        }

        // set hrs
        recordDAO.updateActualHours(serviceId, actualHours);

        // complete service
        recordDAO.complete(serviceId);

        // update advisor availability
        advisorDAO.updateLoad(advisorId, -1);
        
        List<ServiceRecord> activeRecords = recordDAO.findByAdvisorId(advisorId).stream()
            .filter(r -> !r.getStatus().equals("COMPLETED"))
            .toList();
        
        if (activeRecords.isEmpty()) {
            advisorDAO.updateAvailability(advisorId, "AVAILABLE");
        }

        // Get booking and vehicle info
        Booking booking = bookingDAO.findById(record.getBookingId());
        Vehicle vehicle = vehicleDAO.findById(booking.getVehicleId());
        String vehicleInfo = String.format("%s %s (%s)", 
            vehicle.getBrand(), vehicle.getModel(), vehicle.getRegistrationNumber());

        notificationService.sendServiceCompletion(
            vehicle.getUserId(),
            booking.getBookingId(),
            vehicleInfo
        );

    } catch (SQLException e) {
        throw new BusinessLogicException("Error completing service: " + e.getMessage());
    }
}

    public BigDecimal calculateServiceTotal(int serviceId) throws BusinessLogicException {
        try {
            List<ServiceItem> items = serviceItemDAO.findByServiceId(serviceId);
            return items.stream()
                .map(ServiceItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        } catch (SQLException e) {
            throw new BusinessLogicException("Error calculating total: " + e.getMessage());
        }
    }
}