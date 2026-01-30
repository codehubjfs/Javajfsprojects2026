package com.vserv.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.vserv.dao.impl.BookingDAOImpl;
import com.vserv.dao.impl.BookingHistoryDAOImpl;
import com.vserv.dao.impl.CatalogDAOImpl;
import com.vserv.dao.impl.VehicleDAOImpl;
import com.vserv.dao.interfaces.BookingDAO;
import com.vserv.dao.interfaces.BookingHistoryDAO;
import com.vserv.dao.interfaces.CatalogDAO;
import com.vserv.dao.interfaces.VehicleDAO;
import com.vserv.exception.BusinessLogicException;
import com.vserv.model.Availability;
import com.vserv.model.Booking;
import com.vserv.model.Catalog;
import com.vserv.model.Vehicle;
import com.vserv.util.FieldValidator;

/**
 * Booking service
 * 
 * @author Mahesh R
 */
public class BookingService {
	private BookingDAO bookingDAO;
	private CatalogDAO catalogDAO;
	private BookingHistoryDAO historyDAO;
	private NotificationService notificationService;
	private VehicleDAO vehicleDAO;

	public BookingService() {
		this.bookingDAO = new BookingDAOImpl();
		this.catalogDAO = new CatalogDAOImpl();
		this.historyDAO = new BookingHistoryDAOImpl();
		this.notificationService = new NotificationService();
		this.vehicleDAO = new VehicleDAOImpl();
	}

	public List<Catalog> getAllServices() throws BusinessLogicException {
		try {
			return catalogDAO.findAllServices();
		} catch (SQLException e) {
			throw new BusinessLogicException(e.getMessage());
		}
	}

	public List<Catalog> getServicesByCarType(String carType) throws BusinessLogicException {
		try {
			return catalogDAO.findByCarType(carType);
		} catch (SQLException e) {
			throw new BusinessLogicException(e.getMessage());
		}
	}

	public List<Availability> getAvailableSlots(LocalDate fromDate) throws BusinessLogicException {
		try {
			if (!FieldValidator.isFutureDate(fromDate) && !fromDate.equals(LocalDate.now())) {
				throw new BusinessLogicException("Cannot book for past dates");
			}
			return bookingDAO.findAvailableSlots(fromDate);
		} catch (SQLException e) {
			throw new BusinessLogicException("Error fetching available slots: " + e.getMessage());
		}
	}

	public List<Booking> getBookingsByUser(int userId) throws BusinessLogicException {
		try {
			return bookingDAO.findByUserId(userId);
		} catch (SQLException e) {
			throw new BusinessLogicException(e.getMessage());
		}
	}

	/**
	 * Create new booking
	 */
	public Booking createBooking(int vehicleId, int catalogId, LocalDate serviceDate, 
	                             String timeSlot, String notes) throws BusinessLogicException {
		try {
			if (!FieldValidator.isFutureDate(serviceDate) && !serviceDate.equals(LocalDate.now())) {
				throw new BusinessLogicException("Cannot book for past dates");
			}

			Catalog service = catalogDAO.findById(catalogId);
			if (service == null || !service.isActive()) {
				throw new BusinessLogicException("Service not available");
			}

			Booking booking = new Booking();
			booking.setVehicleId(vehicleId);
			booking.setCatalogId(catalogId);
			booking.setServiceDate(serviceDate);
			booking.setTimeSlot(timeSlot);
			booking.setBookingStatus("PENDING");
			booking.setBookingNotes(notes);

			booking = bookingDAO.insert(booking);

			Vehicle vehicle = vehicleDAO.findById(vehicleId);
			String vehicleInfo = String.format("%s %s (%s)", vehicle.getBrand(), 
			                                   vehicle.getModel(), vehicle.getRegistrationNumber());

			historyDAO.logCreated(booking.getBookingId(), vehicle.getUserId(), 
			                     serviceDate, timeSlot);

			notificationService.sendBookingConfirmation(vehicle.getUserId(), 
			    booking.getBookingId(), vehicleInfo, service.getServiceName(), 
			    serviceDate.toString(), timeSlot);

			return booking;

		} catch (SQLException e) {
			throw new BusinessLogicException("Booking failed: " + e.getMessage());
		}
	}

	public void rescheduleBooking(int bookingId, LocalDate newServiceDate, 
	                              String newTimeSlot) throws BusinessLogicException {
		try {
			if (!FieldValidator.isFutureDate(newServiceDate) && 
			    !newServiceDate.equals(LocalDate.now())) {
				throw new BusinessLogicException("Cannot reschedule to past dates");
			}

			Booking booking = bookingDAO.findById(bookingId);
			if (booking == null) {
				throw new BusinessLogicException("Booking not found");
			}

			if (booking.getBookingStatus().equals("CANCELLED") || 
			    booking.getBookingStatus().equals("COMPLETED")) {
				throw new BusinessLogicException("Cannot reschedule " + 
				                          booking.getBookingStatus().toLowerCase() + " booking");
			}

			LocalDate oldDate = booking.getServiceDate();
			String oldSlot = booking.getTimeSlot();

			bookingDAO.reschedule(bookingId, newServiceDate, newTimeSlot, 
			                     oldDate, oldSlot);

			Vehicle vehicle = vehicleDAO.findById(booking.getVehicleId());
			String vehicleInfo = String.format("%s %s (%s)", vehicle.getBrand(), 
			                                   vehicle.getModel(), vehicle.getRegistrationNumber());

			historyDAO.logRescheduled(bookingId, vehicle.getUserId(), 
			    oldDate, oldSlot, newServiceDate, newTimeSlot, 
			    "Customer rescheduled");

			notificationService.sendStatusUpdate(vehicle.getUserId(), bookingId, 
			    vehicleInfo, "RESCHEDULED to " + newServiceDate + " at " + newTimeSlot);

		} catch (SQLException e) {
			throw new BusinessLogicException("Reschedule failed: " + e.getMessage());
		}
	}


	public void cancelBooking(int bookingId) throws BusinessLogicException {
		try {
			Booking booking = bookingDAO.findById(bookingId);
			if (booking == null) {
				throw new BusinessLogicException("Booking not found");
			}

			if (booking.getBookingStatus().equals("CANCELLED")) {
				throw new BusinessLogicException("Booking is already cancelled");
			}

			if (booking.getBookingStatus().equals("COMPLETED")) {
				throw new BusinessLogicException("Cannot cancel completed booking");
			}

			bookingDAO.cancel(bookingId, booking.getServiceDate(), booking.getTimeSlot());

			Vehicle vehicle = vehicleDAO.findById(booking.getVehicleId());

			historyDAO.logCancelled(bookingId, vehicle.getUserId(), 
			                       "Customer requested cancellation");

		} catch (SQLException e) {
			throw new BusinessLogicException("Cancellation failed: " + e.getMessage());
		}
	}
}