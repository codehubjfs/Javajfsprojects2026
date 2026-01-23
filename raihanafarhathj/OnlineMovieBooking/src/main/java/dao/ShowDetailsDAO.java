package dao;

import model.ShowDetails;
import exception.DataAccessException;
import java.util.List;
import java.time.LocalDate;

public interface ShowDetailsDAO {
    int addShow(ShowDetails show) throws DataAccessException;
    ShowDetails getShowById(int showId) throws DataAccessException;
    List<ShowDetails> getShowsByMovie(int movieId, LocalDate date) throws DataAccessException;
    List<ShowDetails> getShowsByHall(int hallId, LocalDate date) throws DataAccessException;
    boolean updateShowStatus(int showId, String status) throws DataAccessException;
    List<ShowDetails> getActiveShowsByMovie(int movieId, LocalDate date) throws DataAccessException;


}
