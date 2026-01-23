package dao;

import model.BookingSeat;
import exception.DataAccessException;
import java.util.List;

public interface BookingSeatDAO {
    int addBookingSeat(BookingSeat bookingSeat) throws DataAccessException;
    List<BookingSeat> getSeatsByBooking(int bookingId) throws DataAccessException;
}
