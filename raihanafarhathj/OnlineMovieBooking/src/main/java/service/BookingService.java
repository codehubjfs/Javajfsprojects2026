package service;

import model.Booking;
import exception.NotFoundException;
import exception.ServiceException;
import exception.BookingException;

import java.util.List;

public interface BookingService {
    int createBooking(Booking booking) throws ServiceException, BookingException;
    Booking getBookingById(int bookingId) throws NotFoundException, ServiceException;
    List<Booking> getBookingsByUser(int userId) throws ServiceException;
}
