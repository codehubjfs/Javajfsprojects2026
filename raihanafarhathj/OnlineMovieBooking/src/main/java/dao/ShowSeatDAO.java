package dao;

import model.ShowSeat;
import exception.DataAccessException;
import java.util.List;

public interface ShowSeatDAO {
    int addShowSeat(ShowSeat showSeat) throws DataAccessException;
    List<ShowSeat> getSeatsByShow(int showId) throws DataAccessException;
    boolean updateSeatStatus(int showSeatId, String status) throws DataAccessException;
}
