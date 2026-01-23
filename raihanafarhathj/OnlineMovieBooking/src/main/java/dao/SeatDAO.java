package dao;

import model.Seat;
import exception.DataAccessException;
import java.util.List;

public interface SeatDAO {
    int addSeat(Seat seat) throws DataAccessException;
    List<Seat> getSeatsByHall(int hallId) throws DataAccessException;
}
