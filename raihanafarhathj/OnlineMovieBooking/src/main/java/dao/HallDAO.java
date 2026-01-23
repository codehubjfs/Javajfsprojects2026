package dao;

import model.Hall;
import exception.DataAccessException;
import java.util.List;

public interface HallDAO {
    int addHall(Hall hall) throws DataAccessException;
    List<Hall> getHallsByTheatre(int theatreId) throws DataAccessException;
}
