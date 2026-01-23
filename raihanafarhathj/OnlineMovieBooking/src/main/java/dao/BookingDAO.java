package dao;

import model.Booking;
import exception.DataAccessException;
import java.util.List;

public interface BookingDAO {
    int addBooking(Booking booking) throws DataAccessException;
    Booking getBookingById(int bookingId) throws DataAccessException;
    List<Booking> getBookingsByUser(int userId) throws DataAccessException;
}
