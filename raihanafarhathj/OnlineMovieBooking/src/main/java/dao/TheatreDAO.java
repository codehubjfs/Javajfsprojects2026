package dao;

import model.Theatre;
import exception.DataAccessException;
import java.util.List;

public interface TheatreDAO {
    int addTheatre(Theatre theatre) throws DataAccessException;
    Theatre getTheatreById(int theatreId) throws DataAccessException;
    List<Theatre> getTheatresByCity(int cityId) throws DataAccessException;
    boolean updateTheatreStatus(int theatreId, String status) throws DataAccessException;
}
