package serviceimpl;

import service.BookingService;
import dao.BookingDAO;
import dao.impl.BookingDAOImpl;
import model.Booking;
import exception.DataAccessException;
import exception.NotFoundException;
import exception.ServiceException;
import exception.BookingException;

import java.util.List;
import java.util.stream.Collectors;

public class BookingServiceImpl implements BookingService {

    private final BookingDAO bookingDAO;

    public BookingServiceImpl() {
        this.bookingDAO = new BookingDAOImpl();
    }

    @Override
    public int createBooking(Booking booking) throws ServiceException, BookingException {
        try {
            if (booking == null) {
                throw new BookingException("Cannot create null booking.");
            }
            if (booking.getCustomerId() <= 0 || booking.getShowId() <= 0) {
                throw new BookingException("Invalid customer or show ID.");
            }
            if (booking.getTotalAmount() <= 0) {
                throw new BookingException("Total amount must be positive.");
            }
            return bookingDAO.addBooking(booking);
        } catch (DataAccessException ex) {
            throw new ServiceException("Failed to create booking: " + ex.getMessage());
        }
    }

    @Override
    public Booking getBookingById(int bookingId) throws NotFoundException, ServiceException {
        try {
            Booking booking = bookingDAO.getBookingById(bookingId);
            if (booking == null) {
                throw new NotFoundException("Booking with ID " + bookingId + " not found.");
            }
            return booking;
        } catch (DataAccessException ex) {
            throw new ServiceException("Failed to fetch booking: " + ex.getMessage());
        }
    }

    @Override
    public List<Booking> getBookingsByUser(int userId) throws ServiceException {
        try {
            List<Booking> bookings = bookingDAO.getBookingsByUser(userId);
            // Sort by booking time descending (most recent first)
            return bookings.stream()
                    .filter(b -> b != null)
                    .sorted((b1, b2) -> b2.getBookingTime().compareTo(b1.getBookingTime()))
                    .collect(Collectors.toList());
        } catch (DataAccessException ex) {
            throw new ServiceException("Failed to fetch bookings for user: " + ex.getMessage());
        }
    }
}
